//document.addEventListener("DOMContentLoaded", function() {

    const listChocolateTemplateText = document.querySelector(".listChocolateTemplate");
    const listChocolateTemplate = Handlebars.compile(listChocolateTemplateText.innerText);

    const chocolates =  document.querySelector(".chocolates");

    axios.get("/api/chocolates")
        .then(function(result) {
                  chocolates.innerHTML = listChocolateTemplate({chocolates  : result.data})
          })
          .catch(function(err){
            console.log(err);
          });

//});



