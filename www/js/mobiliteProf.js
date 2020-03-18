let table = $('#tableMobiliteProf').DataTable();
let lastId="";

function chargerPage() {
    table = $('#tableMobiliteProf').DataTable();
    table.rows().remove().draw();
    $.ajax({
        url: '/',
        type: 'POST',
        data: { action: "listerEtudiants" },
        beforeSend: function(){
            waitSpinner();
        },
        success: function(reponse){
            afficherEtudiantsMobiliteProf(JSON.parse(reponse));
        },
        error: function (e) {
            // en cas d'erreur
            //console.log(e.message);
        },
        complete: function () {
            $('html').css('cursor', 'default');
            $('#waitSpinner').remove();
        }
    });
}

function afficherEtudiantsMobiliteProf(etudiants) {
    for (let i = 0; i < etudiants.length; i++) {
        $.ajax({
            url: '/',
            type: 'POST',
            data: {
                action: "getMobiliteByPseudo", demandeProf: "oui", pseudoEtudiant: etudiants[i].pseudo
            },
            success: function (rep) {
                affichageMobParStudent(etudiants[i], JSON.parse(rep));
            }
        });

    }
    table.on('click', 'td', function (e) {
        if (e.currentTarget.cellIndex == 9) {
            if (e.target.name === "buttonConfirmerMobProf") {
                if(lastId!==e.target.id){
                    lastId=e.target.id;
                    $.ajax({
                        url: '/',
                        type: 'POST',
                        data: {
                            action: "validerMob", id: e.target.id, etat: "valide"
                        },
                        success: function (rep) {
                            chargerPage();
                        }
                    });
                    console.log(e);
                }
               
            } else {
                $.ajax({
                    url: '/',
                    type: 'POST',
                    data: {
                        action: "getMobById", id: e.target.id
                    },
                    beforeSend: function(){
                        waitSpinner();
                    },
                    success: function (rep) {
                    	idMobToDelete = e.target.id;
                        let donneeDoc = JSON.parse(rep);
                        gestionDocumentRob(donneeDoc);
                    },
                    complete: function () {
                        $('html').css('cursor', 'default');
                        $('#waitSpinner').remove();
                    }
                });
            }
        }
    });
}

function affichageMobParStudent(etudiant, mobs) {
    for (var j = 0; j < mobs.length; j++) {
    	if(mobs[j].mobiliteAnnule){
    		continue;
    	}
        var parten;
        if (mobs[j].docDepart == 0 && mobs[j].etat == "CREE") {
            var buttonAdd = "<button type='button' class='btn btn-success' name='buttonConfirmerMobProf' id='" + mobs[j].id + "'>Valider</button>";
        }
        else {
            var buttonAdd = "<button type='button' class='btn btn-success' name='buttonMoveDocument' id='" + mobs[j].id + "'>Gestion doc</button>";
        }
        if (mobs[j].partenaire == "null") {
            parten = "/";
        }
        else {
            parten = mobs[j].partenaire;
        }
        let paiementAller = false;
        let paiementRetour = false;
        if (mobs[j].etat !== "CREE" && mobs[j].etat!=="EN_PREPARATION" && mobs[j].idPays!=221) {
            $.ajax({
                url: '/',
                async:false,
                type: 'POST',
                data: {
                    action: "getDocumentsDepart", idDoc: mobs[j].docDepart
                },
                success: function (rep) {
                    let donneeDoc = JSON.parse(rep);
                    if (donneeDoc['paiementEffectue'] === true) {
                        paiementAller = true;
                    }
                }

            });
            $.ajax({
                url: '/',
                async:false,
                type: 'POST',
                data: {
                    action: "getDocumentsRetour", idDoc: mobs[j].docRetour
                },
                success: function (rep) {
                    let donneeDoc = JSON.parse(rep);
                    if (donneeDoc['paiementEffectue'] === true) {
                        paiementRetour = true;
                    }
                }
            });
            if (mobs[j].etat === "A_PAYER") {
                if (paiementAller === true) {
                    buttonPaiementAller = "PAIEMENT_EFFECTUE";
                } else {
                    buttonPaiementAller = "EN_ATTENTE";
                }
                buttonPaiementRetour = "/";
            } else if (mobs[j].etat === "A_PAYER_SOLDE") {
                if (paiementAller === true) {
                    buttonPaiementAller = "PAIEMENT_EFFECTUE";
                } else {
                    buttonPaiementAller = "EN_ATTENTE";
                }
                if (paiementRetour === true) {
                    buttonPaiementRetour = "PAIEMENT_EFFECTUE";
                } else {
                    buttonPaiementRetour = "EN_ATTENTE";
                }
            }
        } else {
            buttonPaiementAller = "/";
            buttonPaiementRetour = "/";
        }

        table.row.add({
            "0": etudiant.nom + " " + etudiant.prenom,
            "1": mobs[j].niveauPreference,
            "2": mobs[j].typeMobilite,
            "3": mobs[j].SMSSMP,
            "4": mobs[j].periode,
            "5": parten,
            "6": buttonPaiementAller,
            "7": buttonPaiementRetour,
            "8": mobs[j].etat,
            "9": buttonAdd
        }).draw();
    }
}

function addElem(elem) {
    console.log(elem);
}

function chargerScriptMobiliteProf(){
    chargerPage();
}

//------------------------------------------------------------------------------

chargerScriptMobiliteProf();