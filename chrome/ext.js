function menuOnClick(info, tab) {
    xhttp = new XMLHttpRequest(); 

    xhttp.onreadystatechange = function() {
        if (xhttp.readyState == XMLHttpRequest.DONE) {

            chrome.tabs.getSelected(null, function(tab) {
                    chrome.tabs.executeScript(tab.id, {"code" : 'document.body.style.cursor = "auto";'});
            });

            chrome.tabs.create({url: xhttp.responseText});
        }
    }

    xhttp.open("POST", "http://a88ba2de.ngrok.io/?words=" + info.selectionText, true);
    xhttp.send();

    chrome.tabs.getSelected(null, function(tab) {
            chrome.tabs.executeScript(tab.id, {"code" : 'document.body.style.cursor = "wait";'});
    });
}

chrome.contextMenus.create({"title": "Sign with Handy", 
                            "contexts":["selection"], 
                            "onclick": menuOnClick});
