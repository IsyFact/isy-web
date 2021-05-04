export function addHandlersToSidebar(){
    const $sidebarCollapse = $('#sidebarCollapse');
    $sidebarCollapse.on('click', function () {
        $('#sidebar').toggleClass('active');
        $sidebarCollapse.toggleClass('fa-angle-double-right');
        $sidebarCollapse.toggleClass('fa-angle-double-left');
    });
}