function menuOnClick(info, tab) {
    xhttp = new XMLHttpRequest(); 

    xhttp.onreadystatechange = function() {
        if (xhttp.readyState == XMLHttpRequest.DONE) {
            chrome.tabs.create({url: xhttp.responseText});
        }
    }

    xhttp.open("POST", "http://899eca95.ngrok.io/?words=" + info.selectionText, true);
    xhttp.send();

    console.log(info.selectionText);
}

chrome.contextMenus.create({"title": "Sign with Handy", 
                            "contexts":["selection"], 
                            "onclick": menuOnClick});
