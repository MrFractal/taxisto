/**
 * Created by petr on 07.10.2014.
 */
$(document).ready(function() {
    $(function() {
        $("#search").autocomplete({
            source : function(request, response) {
                $.ajax({
                    url : "http://localhost:8080/bla/findAddress",
                    type : "GET",
                    data : {
                        term : request.term
                    },
                    dataType : "json",
                    success : function(data) {
                        response(data);
                    }
                });
            }
        });
    });
});