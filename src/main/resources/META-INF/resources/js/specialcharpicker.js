$(function () {
   'use strict';

    /**
     * Helper function to create an jquery event handler which will
     * be executed in a given context (by default the context of an
     * jquery event handler is the html element
     *
     * @param {Function} fn The function you want to use a an event handler
     * @param {Object} context The desired execution context
     * @return {Funtion} The delegating event handler function
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
     * @class SpecialCharacterPopup
     */
    var SpecialCharacterPopup = function () {
        this.$masterOverlayEl = $('.special-char-picker-widget');
        this.$overlayEl = this.$masterOverlayEl;
        this.$filterItems = $('.filter-item');
        this.$charItems = $('.special-char');
        this.initEvents();
        
        var filterMap = {};
        this.$filterItems.each(function (index, item) {
            var filter = $(item).data().filter || '*';
            filterMap[filter] = index;
        });
        this.filterMap = filterMap;
        
        //this.$overlayEl.draggable();
    };

    SpecialCharacterPopup.prototype = {

        /**
         * Opens the special character popup
         */
        show: function ($inputEl) {
            if ($inputEl) {
              var $listpickerContainer = $(".listpicker-container");
              $listpickerContainer.each(function () {
                $(this).removeClass('open');
              });
              
                var top = 30;
                var left = 0;
                var screencenter = $(window).width() / 2;
                var overlayleft = screencenter - (this.$overlayEl.width() / 2);
                var overlayoffset = 0;
                var overlayposition = 0;
            
                this.$inputEl = $inputEl;
                
                $inputEl.addClass('charpicker-focused');
                
                this.calculateInputPosition();
                this.$inputEl.next().after(this.$overlayEl);
                
                this.selectFilter('*');
                
                this.$overlayEl.show();

                overlayoffset = this.$overlayEl.offset().left;
                overlayposition = this.$overlayEl.position().left;
                left = overlayleft - overlayoffset + overlayposition;

                this.$overlayEl.css({
                    left: left,
                    top: top
                });

                this.$overlayEl.focus();
                
                if ($('.modal-dialog').length) {
                    this.scrollModal();
                } else {
                    this.scroll();
                }
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
            if(this.$inputEl !== undefined && this.$inputEl !== null){
                this.$inputEl.removeClass('charpicker-focused');
                }
            this.$inputEl = null;
            this.$overlayEl.hide();
        },

        getWidget: function () {
            return this.$overlayEl;
        },        
        
        getName: function () {
            return "SpecialCharPickerWidget";
        },        

        isActive: function () {
            return this.$inputEl !== null;
       },

       activate: function () {
           this.$overlayEl.focus();
           this.foreground();
       },

       restoreMasterPopup: function () {
         this.close();
           $('body').after(this.$overlayEl);
       },

       
       calculateInputPosition: function() {
        
            if (this.$inputEl != document.activeElement && (navigator.appVersion.indexOf('MSIE 8') != -1 || navigator.appVersion.indexOf('MSIE 9') != -1))
            {
                this.inputPos = this.$inputEl.val().length + 1;
            }
            else
            {
                this.inputPos = this.$inputEl.caret().begin;
            }

        },
        
        recalculateInputPosition: function() {
            
           this.inputPos = this.$inputEl.caret().begin;
        },
        
        activated: function() {

             return this.hasFocus(this.$overlayEl);
         },

         deactivated: function() {

             return !this.activated();
         },
         
         hasFocus: function($elem) {
            if($elem.is($(document.activeElement))) {
               return true;
            }
               return false;
         },
         
         scroll: function() {
             $('html, body').animate({
                 scrollTop: this.$inputEl.offset().top
             }, 1000);
         },
         
         scrollModal: function() {
             $('.modal').animate({
                 scrollTop: this.$inputEl.offset().top
             }, 1000);
         },
         
         background: function() {
            $(".filter-bar").css('opacity', '0.5');
            $(".charpicker-chars.special-char-box").css('opacity', '0.5');
            //this.$overlayEl.fadeTo("slow", 0.5);
         },
         
         foreground: function() {

            $(".filter-bar").css('opacity', '1.0');
            $(".charpicker-chars.special-char-box").css('opacity', '1.0');
             //this.$overlayEl.fadeTo("slow", 1.0);
         },

         /**
         * Initialies the event handlers
         * @private
         */
        initEvents: function () {

            this.$overlayEl.click(createEventHandler(function (event, $charEl) { 
               this.foreground();
            }, this));

            this.$overlayEl.focus(createEventHandler(function (event, $charEl) { 
               this.foreground();
            }, this));

            this.$overlayEl.mouseover(createEventHandler(function (event, $charEl) {
                if (this.hasFocus(this.$inputEl)) {
                   //this.foreground();
                }
            }, this));

            this.$overlayEl.blur(createEventHandler(function (event, $charEl) { 
                this.background();
            }, this));
            
            this.$charItems.click(createEventHandler(function (event, $charEl) {
                if ($charEl.hasClass("disabled")) {
                    // do nothing
                } else {
                    $('.special-char.active').removeClass('active');
                    $charEl.addClass('active');
                    this.insert($charEl);
                }
            }, this));

            this.$filterItems.click(createEventHandler(function (event, $filterEl) {
                var filter = $filterEl.data().filter || '*';
                var dirOffset = event.shiftKey ? -1 : 1;
                this.selectFilter(filter, dirOffset);
            }, this));

            this.$overlayEl.on('keydown', createEventHandler(function (event, $el) {
                var dirOffset = event.shiftKey ? -1 : 1;
                var keyCode = event.which;

                switch (keyCode) {
                case 13: // [ENTER]
                    this.insert($('.special-char.active'));
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
                    var selectedFilterIndex = this.filterMap[this.selectedFilter];
                    var nextIndex = getInBoundIndex(selectedFilterIndex + dirOffset, this.$filterItems.length);
                    var nextFilter = this.normalizeFilter($(this.$filterItems[nextIndex]).data().filter);
                    this.selectFilter(nextFilter);
                    break;

                case 37: // [<]
                    this.selectCharByFilter(this.selectedFilter, this.selectedCharOffset - 1);
                    break;

                case 38: // [^]
                    if (this.selectedFilter === '*') {
                        var indexInRowAbove;
                        if (this.selectedCharOffset < this.getNumberOfCharsPerRow()) {
                            indexInRowAbove = (this.getNumberRows() * this.getNumberOfCharsPerRow()) + this.selectedCharOffset - 1;
                            if (this.selectedCharOffset === 0) {
                                indexInRowAbove = this.$charItems.length - 1;
                            } else if (indexInRowAbove >= this.$charItems.length){
                                indexInRowAbove = indexInRowAbove -  this.getNumberOfCharsPerRow();
                            }
                        } else {
                            indexInRowAbove = this.selectedCharOffset - this.getNumberOfCharsPerRow();
                        }
                        this.selectCharByFilter(this.selectedFilter, indexInRowAbove);
                    }
                    break;

                case 39: // [>]
                    this.selectCharByFilter(this.selectedFilter, this.selectedCharOffset + 1);
                    break;

                case 40: // [v]
                    if (this.selectedFilter === '*') {
                        var indexInRowBelow = this.selectedCharOffset + this.getNumberOfCharsPerRow();
                        if (indexInRowBelow >= this.$charItems.length) {
                            indexInRowBelow = this.selectedCharOffset % this.getNumberOfCharsPerRow() + 1;
                            if (indexInRowBelow >= this.getNumberOfCharsPerRow()) {
                                indexInRowBelow = 0;
                            }
                        } 
                        this.selectCharByFilter(this.selectedFilter, indexInRowBelow);
                    }
                    break;

                default:
                    // try to select a filter by the pressed key
                    var filter = this.normalizeFilter(String.fromCharCode(keyCode));
                    this.selectFilter(filter, dirOffset);
                    break;
                }

                event.stopPropagation();
                event.preventDefault();
            }, this));
        },

        /**
         * Calculates the number of special characters per row to allow
         * up/down navigation
         * @private
         */
        getNumberOfCharsPerRow: function () {
            if (!this.charItemsPerRow) {
                var num = 0;
                var top = $(this.$charItems[0]).offset().top;
                this.$charItems.each(function (index, item) {
                    if ($(item).offset().top !== top) {
                        num = index;
                        return false; // exit loop
                    }
                });
                this.charItemsPerRow = num;
            }
            return this.charItemsPerRow;
        },

        getNumberRows: function () {
            
            if (!this.numberOfRows) {
                var num = Math.floor((this.$charItems.length / this.getNumberOfCharsPerRow()));
                this.numberOfRows = num;
            }
            return this.numberOfRows;
        },
        
        /**
         * Maps a string to the available filter characters;
         * Defaults to '*' if the input cannot be mapped
         * @private
         */
        normalizeFilter: function (raw) {
            var filter = (raw || '*').toLowerCase();
            if (this.filterMap[filter] >= 0) {
                return filter;
            } else {
                return '*';
            }
        },

        /**
         * Selects the given filter item or the next character associated with that filter
         * if the given filter is already selected
         * @private
         */
        selectFilter: function (filter, dir) {
            if (filter === this.selectedFilter) {
                this.selectCharByFilter(filter, this.selectedCharOffset + dir);
            } else {
                this.selectFilterItem(filter);
            }
        },

        /**
         * Select a filter item by a filter character
         * @private
         */
        selectFilterItem: function (filter) {
            // enable/disable filtered characters
            this.$charItems.each(function (index, el) {
                var $el = $(el);
                var data = $el.data();
                var baseChar = data && data.baseChar;

                if (filter !== '*' && filter !== baseChar) {
                    $el.addClass('disabled');
                } else {
                    $el.removeClass('disabled');
                }
            });

            $('.filter-item.active').removeClass('active');
            $(this.$filterItems[this.filterMap[filter]]).addClass('active');

            this.selectedFilter = filter;
            this.selectCharByFilter(filter);
        },

        /**
         * Select a special character by a given filter value and an offset
         * @private
         */
        selectCharByFilter: function (filter, offset) {
            var $items = filter !== '*' ? $('.special-char[data-base-char=' + filter + ']') : this.$charItems;
            offset = getInBoundIndex(offset, $items.length);

            $('.special-char.active').removeClass('active');
            $($items[offset]).addClass('active');

            this.selectedCharOffset = offset;
        },

        /**
         * Inserts the currently selected character
         * @private
         */
        insert: function ($charEl) {
            var character = $.trim($charEl.text());
            
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
         * Positions the cursor if the inpot element to the given position
         * @private 
         */
        caret: function (pos) {
            this.$inputEl.caret(pos);
            // IE 8 fix
            if (this.$inputEl.caret() != pos)
            {
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

    var SpecialCharacterPopupInitializer = function () {
     this.popup = "";
    };
    
    SpecialCharacterPopupInitializer.prototype = {  
   
    
        initWidget: function(widget) {
            this.popup = widget;
            var popupWidget = this.popup;
        
            window.onclick = function() {
              popupWidget.close();
            };
        },
        
        restoreWidget: function() {
              this.popup.restoreMasterPopup();
        },
        
        refreshWidget: function() {
             var $inputElements = $('input.inputWithSpecialCharPicker[type=text], textarea.inputWithSpecialCharPicker').filter(':not(.specialcharpicker_ajaxtoken)');
             var $openButton = $('.special-char-button').filter(':not(.specialcharpicker_ajaxtoken)');
    
             var $currInputEl;
             var popup = this.popup;

                $inputElements.keydown(function (event) {
                    switch(event.which) {
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
                $inputElements.addClass('specialcharpicker_ajaxtoken');
                $openButton.addClass('specialcharpicker_ajaxtoken');
        },
        
        getName: function() {
            return "SpecialCharPickerInitializer"; 
        }
                

    };

    var widget = new SpecialCharacterPopup();
    
    var specialCharPickerInitializer = new SpecialCharacterPopupInitializer();

    $(document).ready(function() {
    
        // initialize widget on load
        $('.special-char-picker-widget').data("initializer", specialCharPickerInitializer);
        
        specialCharPickerInitializer.initWidget(widget);
        specialCharPickerInitializer.refreshWidget();
        
        // react to AJAX requests
        if(typeof(jsf) != "undefined") {
            // --------------------------------------------------------
            // Ajax-Callback
            // --------------------------------------------------------
            jsf.ajax.addOnEvent(function(callback) {
                
                var charpicker = $('.special-char-picker-widget').data("initializer"); 
                
                if (callback.status === 'begin') {
                  if (charpicker !== undefined) {
                    charpicker.restoreWidget();
                  }
                }

                if (callback.status === 'success') {
                  if (charpicker !== undefined) {
                    charpicker.refreshWidget();
                  }             
                }

            });
        }

    });

});
