import {initSelectlists} from "../../../main/js/widgets/selectlist";


describe('selectlist', function () {
    const interval = 200;
    var container;
    var select;
    var option;

    const getBootstrapSelectList = function () {
        return select.nextSibling;
    };

    const getBootstrapSelectListOption = function () {
        return getBootstrapSelectList().querySelector('li');
    };

    beforeEach(function () {
        jasmine.clock().install();

        option = document.createElement('option');
        option.value = 'value1';
        option.text = 'Value1';

        select = document.createElement('select');
        select.className = 'selectlist';
        select.appendChild(option);

        container = document.createElement('div');
        container.className = 'selectlist';
        container.style.display = 'none';
        container.appendChild(select);

        document.body.append(container);
    });

    afterEach(function () {
        jasmine.clock().uninstall();

        document.body.removeChild(container);
    });

    describe('initSelectlists', function () {

        it('marks select with selectlist_ajaxtoken', function () {
            initSelectlists();

            expect(select).toHaveClass('selectlist_ajaxtoken');
        });

        it('replaces select with bootstrap selectlist', function () {
            initSelectlists();

            expect(select.style.display).toBe('none');
            expect(getBootstrapSelectList()).toHaveClass('bootstrap-selectlist');
            expect(getBootstrapSelectListOption().textContent).toBe(option.text);
        });

        it('refreshes content when parent is visible', function () {
            initSelectlists();

            // change the value of the option tag
            const oldOptionText = option.text;
            option.text += '_xxx';

            // the bootstrap selectlist should still have the old value
            jasmine.clock().tick(interval);
            jasmine.clock().tick(interval);
            expect(getBootstrapSelectListOption().textContent).toBe(oldOptionText);

            // make container and thus the selectlist visible
            container.style.display = 'block';

            // refresh should be called, and the bootstrap select list has the new value
            jasmine.clock().tick(interval);
            expect(getBootstrapSelectListOption().textContent).toBe(option.text);
        });
    });
});
