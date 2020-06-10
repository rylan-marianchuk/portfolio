var rellax = new Rellax('.rellax');

$('.trigger').click(function() {
    $('.side-nav').toggleClass("nav-toggle");
    $('.menu-items').toggleClass("text-link-toggle");
    if (document.title === "Home"){
        $('.name-container').toggleClass("animate-name");
    }
});

function getComments(){
    fetch('/data').then(response => response.text()).then((comments) => {
    document.getElementById('comment-container').innerText = comments;
  });
}