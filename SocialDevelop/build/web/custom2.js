$( "#submitsearch" ).click(function() {
  $(this).closest('#findByKeyWord').submit();
});

function readURL(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                $('#img-profilo')
                    .attr('src', e.target.result);                    
            };
            reader.readAsDataURL(input.files[0]);
        }
}

function remove_skill(param){
   var skill = $.trim($(param).parent().parent().children("a.skill-name").text());
   var userid = $("#userid").val();
   var rm = $(param).parent().parent("li");
   $.ajax({
          datatype:'text/plain',
          type: 'post',
          url: 'rmSkill',
          data: {
           skill:skill,
           userid:userid
          },
          success: function(response) {
            if(response==1){
                rm.remove();
            }
          }
    });              
}


$(".add-skill-btn").on("click",function(){
    var skill = $("#select-skill option:selected");
    var level = $("#select-level option:selected").val();
    var skill_level = skill.val()+":"+level;
    var userid = $("#userid").val();
    var skill_text = skill.text();
    if(skill_text.indexOf("-")>=0){
        skill_text = skill_text.split("-")[1];
    }
    $.ajax({
          datatype: 'text/plain',
          type: 'post',
          url: 'addSkill',
          data: {
           skill_level:skill_level,
           userid:userid
          },
          success: function (response) {
              if(response == 1){
                 
                  
              $(".list-skill").prepend("<li><a class='skill-name' >"+skill_text+"&nbsp;</a><a id='skill-level'>"+level+"</a>\n\
            <span><button onclick='remove_skill(this)' type='button' id='rm-skill-btn' class='btn btn-default btn-sm remove-skill-button rm-skill-btn'><span class='glyphicon glyphicon-remove'></span></button></span></li>");   
            }
         }
        });
    
    });
 
 