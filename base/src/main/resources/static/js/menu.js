var menus = $.ajax({url: contextPath + "/menu/navi", async: false});
alert(menus.responseText);
var SystemMenu =$.parseJSON(menus.responseText);