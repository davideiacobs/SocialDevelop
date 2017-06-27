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
 
 
$(".add-skill-to-task-btn").on("click",function(){  
    //se non ci sono skill nella lista annulliamo il valore dell'input nascosto
    if( $('a.skill-name').length == 0){
        $(".input-skills").val(""); 
    }
    var skill = $("#select-skill option:selected");
    var level = $("#select-level option:selected").val();
    //controlliamo innanzitutto se è stata selezionata una skill e un level
    if(skill.val()!="" && level!=""){
        var skill_level = skill.val()+":"+level;
        console.log(skill_level);
        var skill_text = skill.text();

        if(skill_text.indexOf("-")>=0){
            skill_text = skill_text.split("-")[1];
        }
        //var input_skills = $(".input-skills").val();
        var flag = false;
        //controlliamo se la skill selezionata è già presente nella lista
        var check =  $('a.skill-name:contains("'+skill_text+'")').length;
        if( check > 0){
            flag = true;
        }
        //se non lo è aggiungiamo sia all'input nascosto che alla lista
        if(!flag){
            $(".list-skill").prepend("<li><a class='skill-name' >"+skill_text+"&nbsp;</a>\n\
            <a id='skill-level'>"+level+"</a>\n\
            <span><button onclick='remove_skill_from_task(this)' \n\
            type='button' id='rm-skill-from-task-btn' class='btn btn-default btn-sm \n\
            remove-skill-from-task-button rm-skill-from-task-btn'><span class='glyphicon\n\
             glyphicon-remove'></span></button></span></li>");   
        }     
    }
});



function remove_skill_from_task(param){
    var skill = $.trim($(param).parent().parent().children("a.skill-name").text());
    console.log(skill);
    var rm = $(param).parent().parent("li");
    rm.remove();
}          
 
function reset_popup(){
    $(".list-skill").empty();
    $(".add-task-form")[0].reset();
}


$("a.add-task").on("click", function(){
    $("#popup1").removeClass("hidden");
    reset_popup();
}); 


function remove_task(param){
    $(param).parent("div").parent("li").remove();
}


function aggiungi_task(name,start,end,descr,ncoll, skill_level){
    $(".task-aggiunti").append("<li><div class='gt-text grade-padding'>\n\
                                <p>Task: "+name+"</p>\n\
                                <p>Start Date: "+start+" &nbsp; - &nbsp; End Date: "+end+"</p>\n\
                                <p>Description: "+descr+"</p>\n\
                                <p>Collaborators: "+ncoll+"</p>\n\
                                <p id='append_here_"+name+"'>Skills Richieste: </p>\n\
                                <input type='button' value='Update' id='update-skill' onclick='update_task(this)'/>\n\
                                <input type='button' value='Delete' id='delete-skill' onclick='remove_task(this)' /></div></li>");
    var i = 0;
    for(i;i<skill_level.length;i++){
        var skill_name = skill_level[i].split(":")[0];
        var level = skill_level[i].split(":")[1];
        $("#append_here_"+name).append(skill_name+"("+level+") ; ");
    }
    
}
    
$("#submit-task").on("click", function(){
    $("input").each(function(){
       $(this).removeClass("req"); 
    });
    var task_name = $("#task_name").val();
    var start_date = $("#start_date").val();
    var end_date = $("#end_date").val();
    var task_descr = $("#task_descr").val();
    var num_collaborators = $("#num_collaborators").val();
    var skill_level = [];
    if(task_name!=""){
        if(start_date != ""){
            if(end_date != ""){
                if(task_descr != ""){
                    if(num_collaborators != ""){
                        if($(".list-skill").children("li").length>0){
                            //recuperiamo ora la lista delle skill e dei livelli inseriti
                            $(".list-skill").children("li").each(function (){
                                var skill_name = $.trim($(this).children("a.skill-name").text());
                                var level = $.trim($(this).children("a#skill-level").text());
                                skill_level.push(skill_name+":"+level);
                            });
                            $("#popup1").addClass("hidden");
                            aggiungi_task(task_name,start_date,end_date,task_descr, num_collaborators, skill_level);
                        }else{
                            $(".skills-err").addClass("req-list");
                        }
                    }else{
                        $("#num_collaborators").addClass("req");
                    }
                }else{
                    $("#task_descr").addClass("req");
                    
                }
            }else{
                $("#end_date").addClass("req");
            }
        }else{
            $("#start_date").addClass("req");
        }
    }else{
        $("#task_name").addClass("req");
    }
});
