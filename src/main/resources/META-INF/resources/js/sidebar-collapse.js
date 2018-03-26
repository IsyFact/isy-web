$(document).ready(function () {
    'use strict';
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
        $('#sidebarCollapse').toggleClass('fa-angle-double-right');
        $('#sidebarCollapse').toggleClass('fa-angle-double-left');
    });
});