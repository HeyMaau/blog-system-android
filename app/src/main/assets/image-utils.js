function addImageOnClick() {
    let imgTags = document.getElementsByTagName('img');
    for (let i = 0; i < imgTags.length; i++) {
        console.log(imgTags[i].src)
        imgTags[i].onclick = function () {
            window.img_api.openImagePreview(imgTags[i].src)
        }
    }
}
