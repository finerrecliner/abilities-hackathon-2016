
function menuOnClick(info, tab) {
    var searchstring = info.selectionText;

    xhttp = new XMLHttpRequest(); 
    xhttp.open("POST", "http://899eca95.ngrok.io/?words=" + info.selectionText, true);
    xhttp.send();

    console.log(info.selectionText);
//  console.log("item " + info.menuItemId + " was clicked");
//  console.log("info: " + JSON.stringify(info));
//  console.log("tab: " + JSON.stringify(tab));

}

chrome.contextMenus.create({"title": "Sign with Handy", 
                            "contexts":["selection"], 
                            "onclick": menuOnClick});
