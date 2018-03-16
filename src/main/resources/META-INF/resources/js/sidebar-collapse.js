$(document).ready(function () {
    'use strict';
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
        $('#sidebarCollapse').toggleClass('fa fa-angle-double-right');
        $('#sidebarCollapse').toggleClass('fa fa-angle-double-left');
    });
});