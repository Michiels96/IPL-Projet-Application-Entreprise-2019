function connexion(){
    let jsonUser = form2Json($('#formLogin'));
    if(jsonUser === false){
        notif.text("Il reste des champs vide ");
    } 
    else{
        $.ajax({
            url: '/',
            type: 'POST',
            data: {
                action: 'login',
                infoInscription: jsonUser
            },
            beforeSend: function(){
                waitSpinner();
            },
            success(req){
                //location.reload();
                //décharger le chargement précedent
                retirerTousLesEvenements();
                //charger le chargement en tant qu'utilisateur authentifié
                chargementScripts();
                if(user.getRole()==='E'){
                	notifierDesAnnulations(user.getPseudo());
                }
            },
            error(req){
                if(req.status == 403){
                    $("[name=notification]").empty();
                    $("[name=notification]").append('<div class="alert alert-warning" role="alert">Erreur, mauvais compte et/ou mot de passe,\nrecommencez svp.</div>');
                }
            },
            complete: function(){
                $('html').css('cursor','default');
                $('#waitSpinner').remove();
            }
        });
    }
}

function chargerScriptLogin(){
    $('#pseudoInput').focus();
    $('#pseudoInput').on('keyup', function(e){
        if(e.keyCode == 13){
            connexion();
        }
    });

    $('#mdpInput').on('keyup', function(e){
        if(e.keyCode == 13){
            connexion();
        }
    });

    $('#boutonConnexion').on('click', function(){
        connexion();
    });

    $('#boutonInscription').on('click', function(){
        $("[name=notification]").empty();
        displayPage($('#pageInscription'));
    });
}

function notifierDesAnnulations(pseudo){
	 $.ajax({
         url: '/',
         type: 'POST',
         data: {
             action: 'notifications',
             pseudoEtud: pseudo
         },
         success(req){
        	 console.log(req);
        	 if(req!=="rien"){
        		 alert(req);
        	 }
         },
         error(req){
             console.log("problème de notifications");
         }
     });
}

//------------------------------------------------------------------------------

chargerScriptLogin();