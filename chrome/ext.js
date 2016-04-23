
function menuOnClick(info, tab) {
//  console.log("item " + info.menuItemId + " was clicked");
//  console.log("info: " + JSON.stringify(info));
//  console.log("tab: " + JSON.stringify(tab));

    var searchstring = info.selectionText;

    xhttp = new XMLHttpRequest(); 
    xhttp.open("POST", "http://ec2-52-32-12-200.us-west-2.compute.amazonaws.com/", true);
    xhttp.send("hello world");
}

chrome.contextMenus.create({"title": "Text2ASL", 
                            "contexts":["selection"], 
                            "onclick": menuOnClick});
