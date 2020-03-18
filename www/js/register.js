function inscription(){
    let jsonUser = form2Json($('#formRegister'));
    if(jsonUser === false){
        notif.text("Il reste des champs vides");
    }
    else{
        $.ajax({
            url: '/',
            type: 'POST',
            data: {action: "inscription", infoInscription: jsonUser},
            success: function(reponse){
                notif.text("");
                displayPage($('#pageDeConnexion'));
            },
            error: function(reponse){
                console.log(reponse.responseText);
                switch(reponse.responseText){
                    case "motDePassePasIdentique":
                        notif.text("Les deux mots de passe ne sont pas identiques ");
                        notif.attr('align','center');
                        break;
                    case "adresseMailInvalide":
                        notif.text("L'adresse mail est invalide ");
                        notif.attr('align','center');
                        break;
                    case "pseudoDejaUtilise":
                        notif.text("Le pseudo que vous avez choisis est déjà utilisé, veuillez en choisir un autre svp!");
                        notif.attr('align','center');
                        break;
                    default:
                        notif.text("");
                        displayPage($('#pageDeConnexion'));
                        break;
                }

            },
            error: function(e){
                console.log(e.message);
            }
        });
    }
}

function chargerScriptRegister(){
    $('#creerCompte').on('click', function(){
        inscription();
    });

    $('#dejaCompte').mouseover(function(){
        $(this).css('textDecoration','underline');
        $(this).css('fontWeight','bold');
    });
    $('#dejaCompte').mouseout(function(){
        $(this).css('textDecoration','none');
        $(this).css('fontWeight','normal');
    });

    $('#dejaCompte').on('click', function(){
        notif.text("");
        displayPage($('#pageDeConnexion'));
    });
}

//------------------------------------------------------------------------------

chargerScriptRegister();