
function menuOnClick(info, tab) {
//  console.log("item " + info.menuItemId + " was clicked");
//  console.log("info: " + JSON.stringify(info));
//  console.log("tab: " + JSON.stringify(tab));
  var searchstring = info.selectionText;
  chrome.tabs.create({url: "http://maps.google.com/maps?q=" + searchstring})
}

var id = chrome.contextMenus.create({"title": "Text2ASL", 
                                     "contexts":["selection"], 
                                     "onclick": menuOnClick});
