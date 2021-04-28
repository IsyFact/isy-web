$(function () {
    'use strict';

    /**
     * Helper function to create an jquery event handler which will
     * be executed in a given context (by default the context of an
     * jquery event handler is the html element
     *
     * @param {Function} fn The function you want to use a an event handler
     * @param {Object} context The desired execution context
     * @return {Function} The delegating event handler function
     */
    function createEventHandler(fn, context) {
        return function (event) {
            var ret = fn.call(context, event, $(this));
            event.preventDefault();
            event.stopPropagation();
            return ret;
        };
    }

    /**
     * Bounds a value to given array boundaries so you can loop forwards and
     * backwards by using index +/- 1
     *
     * @param {Number} num The origin number
     * @param {Number} arrayLength Well...
     * @return {Number} The bound value
     *
     * @example
     * getInBoundIndex(0, 4) // returns 0
     * getInBoundIndex(3, 4) // returns 3
     * getInBoundIndex(4, 4) // returns 0
     * getInBoundIndex(5, 4) // returns 1
     * getInBoundIndex(-1, 4) // returns 3
     */
    function getInBoundIndex(num, arrayLength) {
        num = num || 0;
        while (num < 0) {
            num += arrayLength;
        }
        return num % arrayLength;
    }

    /**
     * @class CharPickerPopup
     */
    var CharPickerPopup = function () {
        // Find the master overlay
        this.$masterOverlayEl = $('.charpicker-dinspec');
        // Additional reference
        this.$overlayEl = this.$masterOverlayEl;

        // These are all the selectable Filters. All, by base letter or by group.
        this.$filterItems = $('.charpicker-filter');
        this.$groupFilterItems = this.$filterItems.filter('.charpicker-filter-group');

        // These are all the special characters
        this.$content = $('.charpicker-content');
        this.$charItems = $('.charpicker-special-char');
        this.$activeCharItem = "";

        // Details view for the active Character.
        this.$activeCharPreviewSansSerif = $('.charpicker-preview-sansserif');
        this.$activeCharPreviewSerif = $('.charpicker-preview-serif');
        this.$activeCharName = $('.charpicker-char-as-text');
        this.$activeCharCodepoint = $('.charpicker-char-codepoint');

        this.$returnButton = $('.charpicker-return-button');

        this.$sidebar = $('.charpicker-sidebar');
        this.$collapsibles = this.$sidebar.find('[id$="PanelCollapse"]');

        this.$rowSize = 18;

        // Initialize all the button reference
        this.initEvents();
    };

    CharPickerPopup.prototype = {

        /**
         * Opens the special character popup
         */
        show: function ($inputEl) {
            if ($inputEl) {
                var $listpickerContainer = $(".listpicker-container");
                $listpickerContainer.each(function () {
                    // $(this) is the elements in the dom that is executing this script;
                    // closes dropdowns I suppose
                    $(this).removeClass('open');
                });

                this.$inputEl = $inputEl;

                $inputEl.addClass('charpicker-focused');

                // calculates the position if the caret
                this.calculateInputPosition();
                // puts the thingy after the thingy (?)
                this.$inputEl.next().after(this.$overlayEl);

                // Select group * means deselect all filters and show all characters
                // selectFilter(...) has a second parameter called dir that is
                // not used here
                this.hideIllegalCharacters(this.$inputEl.data().datentyp);
                this.hideIllegalGroups(this.$inputEl.data().datentyp);
                // select the "all" filter
                this.resetActiveCharItem();
                this.selectFilter(this.$filterItems.first());

                // Make char picker visible
                this.$overlayEl.show();

                // Calculate position
                var top = 30;
                var left = ($inputEl.width() - this.$overlayEl.width()) / 2;
                if ($inputEl.offset().left - left < 0) {
                    left = left - $inputEl.offset().left - left;
                }

                // Apply position
                this.$overlayEl.css({
                    left: left,
                    top: top
                });

                // Focus it
                this.$overlayEl.focus();

                // If there are any model dialogs open
                if ($('.modal-dialog').length) {
                    this.scrollModal();
                } else {
                    this.scroll();
                }

            }
        },

        /**
         * Adds the class "illegal" to all characters that are not member of the data type of the input field.
         * @param datentyp data type of the input field
         */
        hideIllegalCharacters: function (datentyp) {
            this.$charItems.removeClass("illegal");
            switch (datentyp) {
                case "DATENTYP_A":
                    this.$charItems.each(function (index, el){
                        var $el = $(el);
                        if ($el.data().charGroup == "N2" || $el.data().charGroup == "N3" ||
                            $el.data().charGroup == "N4" || $el.data().charGroup == "E1" ||
                            $el.data().charGroup == "GRIECHISCH" ||
                            $el.data().charGroup == "KYRILLISCH") {
                            $el.addClass("illegal");
                        }
                    });
                    break;
                case "DATENTYP_B":
                    this.$charItems.each(function (index, el){
                        var $el = $(el);
                        if ($el.data().charGroup == "N3" ||
                            $el.data().charGroup == "N4" || $el.data().charGroup == "E1" ||
                            $el.data().charGroup == "GRIECHISCH" ||
                            $el.data().charGroup == "KYRILLISCH") {
                            $el.addClass("illegal");
                        }
                    });
                    break;
                case "DATENTYP_C":
                    this.$charItems.each(function (index, el){
                        var $el = $(el);
                        if ($el.data().charGroup == "E1" ||
                            $el.data().charGroup == "GRIECHISCH" ||
                            $el.data().charGroup == "KYRILLISCH") {
                            $el.addClass("illegal");
                        }
                    });
                    break;
                case "DATENTYP_D":
                    this.$charItems.each(function (index, el){
                        var $el = $(el);
                        if ($el.data().charGroup == "N4" ||
                            $el.data().charGroup == "KYRILLISCH") {
                            $el.addClass("illegal");
                        }
                    });
                    break;
                default:
                    break;
            }
        },

        /**
         * Adds the classes "illegal" and "disabled" to all groups in the
         * category list that are not member of the data type of the input field
         * @param datentyp data type of the input field
         */
        hideIllegalGroups: function (datentyp) {
            this.$groupFilterItems.removeClass("illegal");
            this.$groupFilterItems.removeClass("disabled");
            switch (datentyp) {
                case "DATENTYP_A":
                    this.$groupFilterItems.each(function (index, el){
                        var $el = $(el);
                        if ($el.html() == "N2" || $el.html() == "N3" ||
                            $el.html() == "N4" || $el.html() == "E1" ||
                            $el.html() == "GRIECHISCH" ||
                            $el.html() == "KYRILLISCH") {
                            $el.addClass("illegal");
                            $el.addClass("disabled");
                        }
                    });
                    break;
                case "DATENTYP_B":
                    this.$groupFilterItems.each(function (index, el){
                        var $el = $(el);
                        if ($el.html() == "N3" ||
                            $el.html() == "N4" || $el.html() == "E1" ||
                            $el.html() == "GRIECHISCH" ||
                            $el.html() == "KYRILLISCH") {
                            $el.addClass("illegal");
                            $el.addClass("disabled");
                        }
                    });
                    break;
                case "DATENTYP_C":
                    this.$groupFilterItems.each(function (index, el){
                        var $el = $(el);
                        if ($el.html() == "E1" ||
                            $el.html() == "GRIECHISCH" ||
                            $el.html() == "KYRILLISCH") {
                            $el.addClass("illegal");
                            $el.addClass("disabled");
                        }
                    });
                    break;
                case "DATENTYP_D":
                    this.$groupFilterItems.each(function (index, el){
                        var $el = $(el);
                        if ($el.html() == "N4" ||
                            $el.html() == "KYRILLISCH") {
                            $el.addClass("illegal");
                            $el.addClass("disabled");
                        }
                    });
                    break;
                default:
                    break;
            }
        },

        /**
         * Closes the special character popup
         */
        hide: function () {
            this.$inputEl.focus();
            this.$inputEl.removeClass('charpicker-focused');
            this.$inputEl = null;
            this.$overlayEl.hide();
        },

        /**
         * Closes the special character popup
         */
        close: function () {
            if (this.$inputEl !== undefined && this.$inputEl !== null) {
                this.$inputEl.removeClass('charpicker-focused');
            }
            this.$inputEl = null;
            this.$overlayEl.hide();
            this.$collapsibles.each(function (index, item) {
                $(item).collapse('hide');
            });
        },

        getWidget: function () {
            return this.$overlayEl;
        },

        getName: function () {
            return "CharPickerDinSpec";
        },

        isActive: function () {
            return this.$inputEl !== null;
        },

        /**
         * Focuses the char picker overlay and calls foreground.
         */
        activate: function () {
            this.$overlayEl.focus();
            this.foreground();
        },

        /**
         * Closes the popup and reattaches it to the body.
         */
        restoreMasterPopup: function () {
            this.close();
            $('body').after(this.$overlayEl);
        },

        /**
         * Calculates the insertions position for the special characters.
         */
        calculateInputPosition: function () {

            if (this.$inputEl != document.activeElement && (navigator.appVersion.indexOf('MSIE 8') != -1 || navigator.appVersion.indexOf('MSIE 9') != -1)) {
                this.inputPos = this.$inputEl.val().length + 1;
            } else {
                this.inputPos = this.$inputEl.caret().begin;
            }

        },

        recalculateInputPosition: function () {
            this.inputPos = this.$inputEl.caret().begin;
        },

        activated: function () {
            return this.hasFocus(this.$overlayEl);
        },

        deactivated: function () {
            return !this.activated();
        },

        /**
         * determines whether the passed in element is currently focused
         * @param $elem
         *          Element that is to be checked
         * @returns {boolean}
         *          true if the element is focused, otherwise false.
         */
        hasFocus: function ($elem) {
            if ($elem.is($(document.activeElement))) {
                return true;
            }
            return false;
        },

        /**
         * scroll page to the input element.
         */
        scroll: function () {
            $('html, body').animate({
                scrollTop: this.$inputEl.offset().top
            }, 1000);
        },

        /**
         * scroll the modal to the input element.
         */
        scrollModal: function () {
            $('.modal').animate({
                scrollTop: this.$inputEl.offset().top
            }, 1000);
        },

        /**
         * set opacity for two elements to 100%
         */
        foreground: function () {
            this.$overlayEl.find('.charpicker-sidebar>*').css('opacity', '1.0');
            this.$overlayEl.find('.charpicker-content-container>*').css('opacity', '1.0');
        },

        /**
         * set opacity for two elements to 50%
         */
        background: function () {
            this.$overlayEl.find('.charpicker-sidebar>*').css('opacity', '0.5');
            this.$overlayEl.find('.charpicker-content-container>*').css('opacity', '0.5');
        },

        /**
         * Initialize the event handlers
         * @private
         */
        initEvents: function () {

            // Move char picker in foreground
            this.$overlayEl.click(createEventHandler(function (event, $charEl) {
                this.foreground();
            }, this));

            // Move char picker in foreground
            this.$overlayEl.focus(createEventHandler(function (event, $charEl) {
                this.foreground();
            }, this));

            // If focus lost move to background
            this.$overlayEl.blur(createEventHandler(function (event, $charEl) {
            }, this));

            // Fix collapsible events!
            this.$collapsibles.each(function (index, elem) {
                var $elem = $(elem);
                $('[href="#' + elem.id + '"]').click(function (event) {
                    $elem.collapse('toggle');
                });
            });

            // Sets character element in the content box active when clicked and sets the details view.
            this.$charItems.click(createEventHandler(function (event, $charEl) {
                if ($charEl.hasClass("disabled")) {
                    // do nothing
                } else {
                    this.setActiveCharItem($charEl);
                }
            }, this));

            // Insert active character when the return button is clicked
            this.$returnButton.click(createEventHandler(function (){
                this.insert(this.$activeCharItem);
            },this));

            this.$filterItems.click(createEventHandler(function (event, $filter) {
                if (!$filter.hasClass('disabled')) {
                    this.selectFilter($filter);
                }
            },this));

            // Keyboard combinations for control inside the char picker
            this.$overlayEl.on('keydown', createEventHandler(function (event, $el) {
                var dirOffset = event.shiftKey ? -1 : 1;
                var keyCode = event.which;

                var $displayedChars;
                var $index;
                var $newActive;

                switch (keyCode) {
                    case 13: // [ENTER]
                        this.insert(this.$activeCharItem);
                        break;

                    case 113: // F2
                    case 27: // [ESC]
                        this.caret(this.inputPos);
                        this.hide();
                        break;

                    case 16: // [SHIFT]
                    case 17: // [CTRL]
                    case 18: // [ALT]
                        // just the modifier key -> ignore
                        break;

                    case 9: // [TAB]
                        // switch to next/previous filter item
                        var selectedFilterIndex = this.$filterItems.index(this.$activeFilter);
                        var nextIndex = getInBoundIndex(selectedFilterIndex + dirOffset, this.$filterItems.length);
                        var $nextFilter = $(this.$filterItems[nextIndex]);
                        // loop till a legal filter is found
                        while ($nextFilter.hasClass('illegal')) {
                            nextIndex = getInBoundIndex(nextIndex + dirOffset, this.$filterItems.length);
                            $nextFilter = $(this.$filterItems[nextIndex]);
                        }
                        this.selectFilter($nextFilter);
                        // open collapsible if filter is inside one
                        var $collapsible = $nextFilter.closest('.panel-collapse');
                        if (!!$collapsible) {
                            $collapsible.collapse('show');
                        }

                        this.scrollIntoView(this.$sidebar, $nextFilter);
                        break;

                    case 37: // [<]
                        $displayedChars = this.getVisibleCharItems();
                        $index = $displayedChars.index(this.$activeCharItem);
                        $newActive = $($displayedChars[Math.max($index - 1, 0)]);
                        this.setActiveCharItem($newActive);
                        this.scrollIntoView(this.$content, $newActive);
                        break;

                    case 38: // [^]
                        if (event.altKey) {
                            this.caret(this.inputPos);
                            this.hide();
                        } else {
                            $displayedChars = this.getVisibleCharItems();
                            $index = $displayedChars.index(this.$activeCharItem);
                            $newActive = $($displayedChars[Math.max($index - this.$rowSize, 0)]);
                            this.setActiveCharItem($newActive);
                            this.scrollIntoView(this.$content, $newActive);
                        }
                        break;

                    case 39: // [>]
                        $displayedChars = this.getVisibleCharItems();
                        $index = $displayedChars.index(this.$activeCharItem);
                        $newActive = $($displayedChars[Math.min($index + 1, $displayedChars.length - 1)]);
                        this.setActiveCharItem($newActive);
                        this.scrollIntoView(this.$content, $newActive);
                        break;

                    case 40: // [v]
                        $displayedChars = this.getVisibleCharItems();
                        $index = $displayedChars.index(this.$activeCharItem);
                        $newActive = $($displayedChars[Math.min($index + this.$rowSize, $displayedChars.length - 1)]);
                        this.setActiveCharItem($newActive);
                        this.scrollIntoView(this.$content, $newActive);
                        break;

                    default:
                        break;
                }

                event.stopPropagation();
                event.preventDefault();
            }, this));
        },

        setDetailsView: function ($charEl) {
            var charData = $charEl.data();
            this.$activeCharPreviewSerif.html(charData.charZeichen);
            this.$activeCharPreviewSansSerif.html(charData.charZeichen);
            this.$activeCharName.html(charData.charName);
            this.$activeCharCodepoint.html(charData.charCodepoint
                .replace(/</g,'&lt;').replace(/>/g,'&gt;'));
        },

        selectFilter: function ($filter) {
            this.$activeFilter = $filter;
            this.highlightFilter($filter);

            if ($filter.hasClass('charpicker-filter-all')) {
                this.deselectAllFilters();
            } else if ($filter.hasClass('charpicker-filter-base')) {
                this.selectBaseFilter($filter.text());
            } else if ($filter.hasClass('charpicker-filter-group')) {
                this.selectGroupFilter($filter.text());
            }

            this.setActiveCharItem(this.getVisibleCharItems().first());
        },

        highlightFilter: function ($filter) {
            this.$filterItems.removeClass('active');
            $filter.addClass('active');
        },

        /**
         * Deselects all filter and displays all characters
         */
        deselectAllFilters: function () {
            this.$charItems.removeClass('hidden');
        },

        /**
         * Hides all characters that do not have the matching base
         * @param base
         *          The base that is used as a selection criteria
         */
        selectBaseFilter: function (base) {
            this.$charItems.each(function (index, el) {
                var $el = $(el);
                var data = $el.data();
                var charBase = data && data.charBase;

                // Sets base to undefined, so all characters without a base are displayed when the category
                // "*" is selected.
                if (base === '*') {
                    base = undefined;
                }

                if (base !== charBase) {
                    $el.addClass('hidden');
                } else {
                    $el.removeClass('hidden');
                }
            });
        },

        /**
         * Hides all characters that do not have the matching group
         * @param group
         *          The group that is used as a selection criteria
         */
        selectGroupFilter: function (group) {
            this.$charItems.each(function (index, el) {
                var $el = $(el);
                var data = $el.data();
                var charGroup = data && data.charGroup;

                if (group !== charGroup) {
                    $el.addClass('hidden');
                } else {
                    $el.removeClass('hidden');
                }
            });
        },

        getVisibleCharItems: function () {
            return this.$charItems.filter(':not(.hidden):not(.illegal)');
        },

        resetActiveCharItem: function () {
            this.$charItems.removeClass('active');
            this.$activeCharItem = "";
        },

        setActiveCharItem: function ($charItem) {
            if (!!this.$activeCharItem) {
                this.$activeCharItem.removeClass('active');
            }
            this.$activeCharItem = $charItem;
            this.$activeCharItem.addClass('active');
            this.setDetailsView(this.$activeCharItem);
        },

        scrollIntoView: function ($context, $item) {
            if (!this.scrollFunc) {
                var isIE = window.navigator.userAgent.match(/(MSIE|Trident)/);
                if (isIE) {
                    this.scrollFunc = function ($context, $item) {
                        // accommodate for paddings
                        var offset = $item.offset().top - $context.offset().top - 1;
                        var scrollPos;
                        if (offset < 0) {
                            scrollPos = $context.scrollTop() + offset;
                            $context.animate({ scrollTop: scrollPos }, 10);
                            return;
                        }

                        console.log($item.offset().top + $item.height());
                        console.log($context.offset().top + $context.height());
                        // accommodate for paddings
                        offset = $item.offset().top + $item.height() - ($context.offset().top + $context.height()) + 6;
                        if (offset > 0) {
                            scrollPos = $context.scrollTop() + offset;
                            $context.animate({ scrollTop: scrollPos }, 10);
                        }
                    };
                } else {
                    this.scrollFunc = function ($context, $item) {
                        $item[0].scrollIntoView({behavior: 'smooth', block: 'nearest'});
                    };
                }
            }
            this.scrollFunc($context, $item);
        },

        /**
         * Inserts the currently selected character
         * @private
         */
        insert: function ($charEl) {
            var character = $charEl.data().charZeichen;

            var inputVal, pre, post, value;

            this.$inputEl.focus();

            this.recalculateInputPosition();

            inputVal = this.$inputEl.val();

            pre = inputVal.substr(0, this.inputPos);
            post = inputVal.substr(this.inputPos, inputVal.length);
            value = pre + character + post;

            this.$inputEl.val(value);
            this.inputPos = this.inputPos + character.length;
            this.caret(this.inputPos);

            // Keep active after insertion of a character
            this.activate();
        },

        /**
         * Positions the cursor if the input element to the given position
         * @private
         */
        caret: function (pos) {
            this.$inputEl.caret(pos);
            // IE 8 fix
            if (this.$inputEl.caret() != pos) {
                if (this.$inputEl[0].createTextRange) {
                    var range = this.$inputEl[0].createTextRange();
                    range.collapse(true);
                    range.moveEnd('character', pos);
                    range.moveStart('character', pos);
                    range.select();
                }
            }
        }

    };

    var CharPickerPopupInitializer = function () {
        this.popup = "";
    };

    CharPickerPopupInitializer.prototype = {

        initWidget: function (widget) {
            this.popup = widget;
            var popupWidget = this.popup;

            window.onclick = function () {
                popupWidget.close();
            };
        },

        restoreWidget: function () {
            this.popup.restoreMasterPopup();
        },

        refreshWidget: function () {
            var $inputElements = $('input.inputWithCharPickerDinSpec[type=text], textarea.inputWithCharPickerDinSpec').filter(':not(.charpickerdinspec_ajaxtoken)');
            var $openButton = $('.charpicker-open-button').filter(':not(.charpickerdinspec_ajaxtoken)');

            var $currInputEl;
            var popup = this.popup;

            $inputElements.keydown(function (event) {
                switch (event.which) {
                    case 40: // alt + arrow down
                        if (event.altKey) {
                            if (popup.isActive()) {
                                popup.activate();
                            } else {
                                popup.show($currInputEl);
                            }
                        }
                        return;

                    case 113: // F2
                        if (popup.isActive()) {
                            popup.activate();
                        } else {
                            popup.show($currInputEl);
                        }
                        break;

                    case 27: // [ESC]
                        if (popup.isActive()) {
                            popup.activate();
                        }
                        event.preventDefault();
                        popup.close();
                        $(this).focus();
                        return;

                    case 9: // Tab
                        popup.close();
                        return;


                    default:
                        if (popup.isActive()) {
                            popup.background();
                        }
                        return;
                }
                event.preventDefault();
            });

            $inputElements.click(function (event) {
                $currInputEl = $(this);
                if ($currInputEl.is(popup.$inputEl)) {
                    popup.background();
                } else {
                    popup.close();
                }
                event.preventDefault();
                event.stopPropagation();
            });


            $openButton.click(function (event) {
                $currInputEl = $(this).prev('input');
                if (popup.isActive()) {
                    if ($currInputEl.is(popup.$inputEl)) {
                        popup.close();
                    } else {
                        // show popup in new position
                        popup.close();
                        popup.show($currInputEl);
                    }
                } else {
                    popup.show($currInputEl);
                }
                event.preventDefault();
                event.stopPropagation();
            });

            $inputElements.on('focus', function () {
                $currInputEl = $(this);
            });

            $openButton.addClass('enabled');
            $inputElements.addClass('charpickerdinspec_ajaxtoken');
            $openButton.addClass('charpickerdinspec_ajaxtoken');
        },

        getName: function () {
            return "SpecialCharPickerInitializer";
        }


    };

    var widget = new CharPickerPopup();

    var charPickerInitializer = new CharPickerPopupInitializer();

    $(document).ready(function () {

        // Initiale Darstellung
        $('.charpicker-dinspec-widget').data("initializer", charPickerInitializer);

        charPickerInitializer.initWidget(widget);
        charPickerInitializer.refreshWidget();

        // Reaktion auf AJAX Requests
        if (typeof (jsf) != "undefined") {
            // --------------------------------------------------------
            // Ajax-Callback
            // --------------------------------------------------------
            jsf.ajax.addOnEvent(function (callback) {

                var initializer = $('.charpicker-dinspec-widget').data("initializer");

                if (callback.status === 'begin') {
                    if (initializer !== undefined) {
                        initializer.restoreWidget();
                    }
                }

                if (callback.status === 'success') {
                    if (initializer !== undefined) {
                        initializer.refreshWidget();
                    }
                }

            });
        }

    });

});
