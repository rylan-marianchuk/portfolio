var rellax = new Rellax('.rellax');

$('.trigger').click(function() {
    $('.side-nav').toggleClass("nav-toggle");
    $('.menu-items').toggleClass("text-link-toggle");
    if (document.title === "Home"){
        $('.name-container').toggleClass("animate-name");
    }
});

function getBirthday(){
    fetch('/birthday').then(response => response.text()).then((day) => {
    document.getElementById('birthday-container').innerText = day;
  });
}