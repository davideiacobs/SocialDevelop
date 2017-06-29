var flagUpdate = false;
var updatedElement;


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
    //$("#popup1").css("display","block")
    reset_popup();
}); 


function remove_task(param){
    $(param).parent("div").parent("li").remove();
}


function reload_task(name,start,end,descr,coll,skills){
    $("#task_name").val(name);
    $("#start_date").val(start);
    $("#end_date").val(end);
    $("#task_descr").val(descr);
    $("#num_collaborators").val(coll);

    var i=0;
    var skl = skills.split(";");
    for(i;i<skl.length-1;i++){
        var splitted = skl[i].split("(");
        var skill_text = splitted[0];
        var level = splitted[1].split(")")[0];
        $(".list-skill").prepend("<li><a class='skill-name' >"+skill_text+"&nbsp;</a>\n\
            <a id='skill-level'>"+level+"</a>\n\
            <span><button onclick='remove_skill_from_task(this)' \n\
            type='button' id='rm-skill-from-task-btn' class='btn btn-default btn-sm \n\
            remove-skill-from-task-button rm-skill-from-task-btn'><span class='glyphicon\n\
             glyphicon-remove'></span></button></span></li>");   
    }
    //cancellare vecchio li solo quando si clicca submit e non nella funzione update_task!!!
    
}


function update_task(param){
    flagUpdate = true;
    var name = $.trim($(param).siblings("p#name").text().split(":")[1]);
    var start = $.trim($(param).siblings("p#start-end").text().split(":")[1].split("-")[0]);
    var end = $.trim($(param).siblings("p#start-end").text().split("-")[1].split(":")[1]);
    var descr = $.trim($(param).siblings("p#descr").text().split(":")[1]);
    var coll = $.trim($(param).siblings("p#coll").text().split(":")[1]);
    var skills = $.trim($(param).siblings("p.skills").text().split(":")[1]);
    //$(param).parent("div").parent("li").remove();
    updatedElement = $(param).parent("div").parent("li");
    $("a.add-task").trigger("click");
    document.location.href = $("a.add-task").attr("href");
    reload_task(name,start,end,descr,coll,skills);
}

function aggiungi_task(name,start,end,descr,ncoll, skill_level, tipo){
    if(flagUpdate==true){
        updatedElement.remove();
        flagUpdate=false;
    }
    
    $(".task-aggiunti").append("<li><div class='gt-text grade-padding'>\n\
                                <p id='name'>Task: "+name+"</p>\n\
                                <p id='tipo'>Type: "+tipo+"</p>\n\
                                <p id='start-end'>Start Date: "+start+" &nbsp; - &nbsp; End Date: "+end+"</p>\n\
                                <p id='descr'>Description: "+descr+"</p>\n\
                                <p id='coll'>Collaborators: "+ncoll+"</p>\n\
                                <p id='append_here_"+name+"' class='skills'>Skills Richieste: </p>\n\
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
    var tipo = $("#select-type option:selected").text();
    var skill_level = [];
    if(task_name!=""){
        if(tipo!=""){
            if(start_date != "" && !isNaN(Date.parse(start_date)) ){   
                if(end_date != "" && !isNaN(Date.parse(end_date))){
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
                                aggiungi_task(task_name,start_date,end_date,task_descr, num_collaborators, skill_level,tipo);
                            }else{
                                $(".skills-err").addClass("req-list");
                                $("#select-skill").focus();
                            }
                        }else{
                            $("#num_collaborators").addClass("req");
                            $("#num_collaborators").focus();
                        }
                    }else{
                        $("#task_descr").addClass("req");
                        $("#task_descr").focus();
                    }
                }else{
                    $("#end_date").addClass("req");
                    $("#end_date").focus();
                }
            }else{
                $("#start_date").addClass("req");
                $("#start_date").focus();
            }
        }else{
            $(".type-err").addClass("req-list");
            $("#select-type").focus();
        }
    }else{
        $("#task_name").addClass("req");
        $("#task_name").focus();
    }
});




$("#submit-project").on("click", function(){
   //azzero lavore input nascosto
   $("#tasks").val("");
   //conto il numero di task aggiunti
   var c = $(".task-aggiunti").children("li").length;
   console.log(c);
   var i=0;
   for(i=0;i<c;i++){
       //controlliamo valore dell'input nascosto
       var input_tasks = $("#tasks").val();
       console.log(input_tasks);
       //recuperiamo tutti i valori del task
       var task = $(".task-aggiunti").children("li")[i];
       var name = $.trim($(task).children().children("p#name").text().split(":")[1]);
       var tipo = $.trim($(task).children().children("p#tipo").text().split(":")[1]);
       var start = $.trim($(task).children().children("p#start-end").text().split(":")[1].split("-")[0]);
       var end = $.trim($(task).children().children("p#start-end").text().split("-")[1].split(":")[1]);
       var descr = $.trim($(task).children().children("p#descr").text().split(":")[1]);
       var coll = $.trim($(task).children().children("p#coll").text().split(":")[1]);
       var skills = $.trim($(task).children().children("p.skills").text().split(":")[1]);
       
       if(input_tasks==""){
           //stiamo inserendo il primo task nell'input
           $("#tasks").val(name+"#"+start+"#"+end+"#"+descr+"#"+coll+"#"+skills+"#"+tipo+"@");
       }else{
           $("#tasks").val(input_tasks+name+"#"+start+"#"+end+"#"+descr+"#"+coll+"#"+skills+"#"+tipo+"@");
       }
   }
    
});



$("#select-type").on("change", function(){
    $("#select-skill option").removeClass("hidden");
    $(".list-skill").empty();
    $("#select-skill").val("");
    var type_id = $("#select-type option:selected").val();
    $("#select-skill option").not("."+type_id).addClass("hidden");
});


$(function(){
  $(".add-project-form")[0].reset();
  $(".add-task-form")[0].reset();
});


window.onbeforeunload = function() {
  $(".add-project-form")[0].reset();
  $(".add-task-form")[0].reset();
};


$("#private-check").on("click", function(){
   
   var value = $("#isPrivate").val(); 
   if(value==0){
       $("#isPrivate").val(1);
   }else{
       $("#isPrivate").val(0);
   }
   console.log($("#isPrivate").val())
});