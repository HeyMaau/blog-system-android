function addImageOnClick() {
    let imgTags = document.getElementsByTagName('img');
    for (let i = 0; i < imgTags.length; i++) {
        imgTags[i].onclick = function () {
            window.img_api.openImagePreview(imgTags[i].src)
        }
    }
}

function updateViewModelContent(innerHTML) {
    window.img_api.updateViewModelContent(innerHTML)
}

function initWebViewImageMap() {
    window.img_api.initWebViewImageMap()
}