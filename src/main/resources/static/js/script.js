$(document).ready(function() {
    $('#toggleButton').click(function() {
        $('#firstRow').toggleClass('collapse');
        $('#secondRow').toggleClass('collapse');
    });
});