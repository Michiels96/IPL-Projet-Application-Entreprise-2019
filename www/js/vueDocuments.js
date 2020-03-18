//Redirection vers documents
let programme = null;
let compteur = 0;
var documentsDepart = null;
var documentsRetour = null;
var versionDepart = null;
var versionRetour = null;
var etatMobilite=null;
var idMobilite=null;
var versionMobilite=null;

function gestionDocumentRob(infoMob){
    setDernierePage("pageDocuments");
    displayPage($('#pageDocuments'));
    programme=infoMob["programme"];
    documentsDepart=infoMob['documentsDepart'];
    documentsRetour=infoMob['documentsRetour'];
    etatMobilite=infoMob['etatMobilite'];
    idMobilite=infoMob['idMobilite'];
    versionMobilite=infoMob['version'];
    getDataDepart();
    getDataRetour();
}

function redirectionDocumentsProf(idDocDepart, idDocRetour){
    resetDataDepartRetour();
    setDernierePage("pageDocuments");
    displayPage($('#pageDocuments'));
    documentsDepart = idDocDepart;
    documentsRetour = idDocRetour;
    getDataDepart();
    getDataRetour();
}

function redirectionDocumentsEtudiant(current){
    resetDataDepartRetour();
    setDernierePage("pageDocuments");
    displayPage($('#pageDocuments'));
    documentsDepart = $(current).parent().parent().parent().parent().parent().attr('documentsDepart');
    documentsRetour = $(current).parent().parent().parent().parent().parent().attr('documentsRetour');
    getDataDepart();
    getDataRetour();
}

function resetDataDepartRetour(){
    $('form[name=form-documents] input[type=checkbox]').removeAttr("checked");
}

function getDataDepart(){
    $.ajax({
        url: '/',
        type: 'POST',
        data: {
            action: 'getDocumentsDepart',
            idDoc: documentsDepart
        },
        success(resp) {
            var docDepart = JSON.parse(resp);
            remplissageDocumentsDepart(docDepart);
        },
        error(req) {
            console.log(req);
        }
    });
}

function getDataRetour(){
    $.ajax({
        url: '/',
        type: 'POST',
        data: {
            action: 'getDocumentsRetour',
            idDoc: documentsRetour
        },
        success(resp) {
            var docRetour = JSON.parse(resp);
            remplissageDocumentsRetour(docRetour);
        },
        error(req) {
            console.log(req);
        }
    });
}

function remplissageDocumentsDepart(docDepart) {
    let current = $('#docDepart');
    versionDepart = docDepart["version"];
    if(programme != "AUTRE" && (etatMobilite === "A_PAYER" || etatMobilite === "A_PAYER_SOLDE")){
        $(current).find('#paiementDepart').removeAttr('hidden');
    }
    if(docDepart["charteEtudiant"]){
        $(current).find($('[name=charteEtudiant]')).attr('checked', 'checked');
    }
    if(docDepart["contratBourse"]){
        $(current).find($('[name=contratBourse]')).attr('checked', 'checked');
    }
    if(docDepart["conventionStageEtude"]){
        $(current).find($('[name=conventionStageEtude]')).attr('checked', 'checked');
    }
    if(docDepart["documentEngagement"]){
        $(current).find($('[name=documentEngagement]')).attr('checked', 'checked');
    }
    if(docDepart["preuveTestLinguistique"]){
        $(current).find($('[name=preuveTestLinguistique]')).attr('checked', 'checked');
    }
    if(docDepart["paiementEffectue"]){
        $(current).find($('[name=confirmationPaiementDepart]')).attr('checked', 'checked');
    }
    if(programme === "AUTRE"){
        $('#pourSuisse').attr('hidden', 'hidden');
    }
}

function remplissageDocumentsRetour(docRetour) {
    let current = $('#docRetour');
    versionRetour = docRetour["version"];
    if(programme != "AUTRE" && etatMobilite === "A_PAYER_SOLDE"){
        $(current).find('#paiementRetour').removeAttr('hidden');
    }
    if(docRetour["attestationSejour"]){
        $(current).find($('[name=attestationSejour]')).attr('checked', 'checked');
    }
    if(docRetour["preuvePassageTest"]){
        $(current).find($('[name=preuvePassageTest]')).attr('checked', 'checked');
    }
    if(docRetour["rapportFinal"]){
        $(current).find($('[name=rapportFinal]')).attr('checked', 'checked');
    }
    if(docRetour["stageReleveNote"]){
        $(current).find($('[name=stageReleveNote]')).attr('checked', 'checked');
    }
    if(docRetour["paiementEffectue"]){
        $(current).find($('[name=confirmationPaiementRetour]')).attr('checked', 'checked');
    }
}

function alertSuccess(){
    $("#alert-success-doc").show();
    // $("#alert-success-doc").hide(5999)
    $("#alert-success-doc").fadeOut(5999);
}

function alertError(){
    $('#alert-danger-doc').show();
    // $("#alert-danger-doc").fadeOut(5999);
    $("#alert-danger-doc").hide(5999);
}

// ------------------------------------------------------------------------------
$('[name=confirmerModifInfoDocuments]').on('click', function(){
    $('#docDepart [type=checkbox]').each(function(indice, element){
        if($(element).attr('checked')){
            compteur++;
        }
    });
    console.log(compteur);
    if(programme === "AUTRE" && compteur > 0){
        $('#docDepart').find($('[name=contratBourse]')).attr('checked', 'checked');
    }
    var docDepart = form2Json($('#docDepart'));
    var docRetour = form2Json($('#docRetour'));
    $.ajax({
        url: '/',
        type: 'POST',
        data: {
            action: 'updateDocumentsDepart',
            json: docDepart,
            idDoc: documentsDepart,
            version: versionDepart,
            idMobilite: idMobilite,
            versionMob: versionMobilite,
            etatMobilite: etatMobilite,
            idDocRetour: documentsRetour
        },
        success(resp) {
            console.log("mise à jour successfull.");
            getDataDepart();
            $.ajax({
                url: '/',
                type: 'POST',
                data: {
                    action: 'updateDocumentsRetour',
                    json: docRetour,
                    idDoc: documentsRetour,
                    version: versionRetour,
                    idMobilite: idMobilite,
                    versionMob: versionMobilite,
                    etatMobilite: etatMobilite,
                    idDocDepart: documentsDepart
                },
                success(resp) {
                    $.ajax({
                        url: '/',
                        type: 'POST',
                        data: {
                            action: "getMobById", id: idMobilite
                        },
                        beforeSend: function () {
                            waitSpinner();
                        },
                        success: function (rep) {
                            let donneeDoc = JSON.parse(rep);
                            gestionDocumentRob(donneeDoc);
                        },
                        complete: function () {
                            $('html').css('cursor', 'default');
                            $('#waitSpinner').remove();
                        }
                    });
                    console.log("mise à jour successfull.");
                    getDataRetour();
                    alertSuccess();
                },
                error(req) {
                    console.log(req);
                    alertError();
                }
            });
        },
        error(req) {
            console.log(req);
            alertError();
        }
    });
});
function chargerScriptVueDocuments(){
    // EnregistrerModification
    $('[name=confirmerModifInfoDocuments]').on('click', function(){
        var docDepart = form2Json($('#docDepart'));
        var docRetour = form2Json($('#docRetour'));
        console.log(docDepart);
        $.ajax({
            url: '/',
            type: 'POST',
            data: {
                action: 'updateDocumentsDepart',
                json: docDepart,
                idDoc: documentsDepart,
                version: versionDepart,
                idMobilite: idMobilite,
                versionMob: versionMobilite,
                etatMobilite: etatMobilite,
                idDocRetour: documentsRetour
            },
            success(resp) {
                console.log("mise à jour successfull.");
                getDataDepart();
                $.ajax({
                    url: '/',
                    type: 'POST',
                    data: {
                        action: 'updateDocumentsRetour',
                        json: docRetour,
                        idDoc: documentsRetour,
                        version: versionRetour,
                        idMobilite: idMobilite,
                        versionMob: versionMobilite,
                        etatMobilite: etatMobilite,
                        idDocDepart: documentsDepart
                    },
                    success(resp) {
                        $.ajax({
                            url: '/',
                            type: 'POST',
                            data: {
                                action: "getMobById", id: idMobilite
                            },
                            beforeSend: function () {
                                waitSpinner();
                            },
                            success: function (rep) {
                                let donneeDoc = JSON.parse(rep);
                                gestionDocumentRob(donneeDoc);
                            },
                            complete: function () {
                                $('html').css('cursor', 'default');
                                $('#waitSpinner').remove();
                            }
                        });
                        console.log("mise à jour successfull.");
                        getDataRetour();
                        alertSuccess();
                    },
                    error(req) {
                        console.log(req);
                        alertError();
                    }
                });
            },
            error(req) {
                console.log(req);
                alertError();
            }
        });
    });

//    $('[name=annulerMobEtudiant]').on('click', function(){
//        var docDepart = form2Json($('#docDepart'));
//        var docRetour = form2Json($('#docRetour'));
//        console.log(docDepart);
//        $.ajax({
//            url: '/',
//            type: 'POST',
//            data: {
//                action: 'annulerMobDefinitif',
//                id: idMobToDelete
//            },
//            success(resp) {
//                console.log("suppression successfull.");
//                erreurChamps("La suppression a bien été faite.","pageVueDocuments");
//            },
//            error(req) {
//                console.log(req);
//                alertError();
//            }
//        });
//    });
}

//------------------------------------------------------------------------------
// EnregistrerModification

$('[name=annulerMobEtudiant]').on('click', function(){
	//var docDepart = form2Json($('#docDepart'));
    //var docRetour = form2Json($('#docRetour'));
    $('#modal').modal('show.bs.modal');
});

$('#submitAnnulation').on('click', function() {
	let msg = $('#inputModal').val();
    $('#modal').remove();
    $.ajax({
        url: '/',
        type: 'POST',
        data: {
            action: 'annulerMobDefinitif',
            id: idMobToDelete,
            message: msg
        },
        success(resp) {
            console.log("suppression successfull.");
            erreurChamps("La suppression a bien été faite.","pageVueDocuments");
        },
        error(req) {
            console.log(req);
            alertError();
        }
    });
});

$(function(){
	var availableMessages = [
	      "Cas de force majeur",
	      "Plus de places disponibles",
	      "Choix venant de l'étudiant",
	      "Evénement externe exceptionnel"
	    ];
    $("#inputModal").autocomplete({
    	source: availableMessages
    });
});



chargerScriptVueDocuments();