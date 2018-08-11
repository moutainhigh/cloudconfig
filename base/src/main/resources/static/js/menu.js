var menus = $.ajax({url: contextPath + "/menu/navi", async: false});
var SystemMenu =$.parseJSON(menus.responseText);