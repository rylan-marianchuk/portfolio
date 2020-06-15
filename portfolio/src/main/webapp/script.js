var rellax = new Rellax('.rellax');

$('.trigger').click(function() {
    $('.side-nav').toggleClass("nav-toggle");
    $('.menu-items').toggleClass("text-link-toggle");
    if (document.title === "Home"){
        $('.name-container').toggleClass("animate-name");
    }
});


/** Retrieve comments from the data store */
function getComments() {
  fetch('/list-comments').then(response => response.json()).then((comments) => {

    const commentList = document.getElementById('comment-list');
    console.log(commentList);
    comments.forEach((comment) => { commentList.appendChild(showComment(comment));})
  });
}

/** Display a given comment withtin the list on index */
function showComment(comment){
    const COMMENT = document.createElement('li');

    COMMENT.className = 'comment';

    COMMENT.innerText = comment;
    return COMMENT;
}
