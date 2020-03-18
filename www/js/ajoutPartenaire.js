function verifFormPartenaire(form){
    console.log("cc");
    if(form.find($('[name=adresse]')).val() == "" || form.find($('[name=nomLegal]')).val() == "" || form.find($('[name=nomBuisness]')).val() == "" || form.find($('[name=nomCompletLegal]')).val() == "" || form.find($('[name=ville]')).val() == "" || form.find($('[name=codePostal]')).val() == "" || form.find($('[name=phone]')).val() == "" || form.find($('[name=mail]')).val() == "") {
        $('#notificationAjoutPartenaireError').attr('hidden', false);
        $('#notificationAjoutPartenaireError').text('Des champs obligatoires ne sont pas rempli');
        return false;
    }
    $('#notificationAjoutPartenaireError').attr('hidden', true);
    return true;
}

function chargerScriptAjoutPartenaire(){
    $.ajax({
        url: '/',
        type: 'POST',
        async: false,
        data: {
            action: 'getAllPays'
        },
        success(resp) {
            let map = JSON.parse(resp);
            let select = $('#patternCardAjoutPartenaire').find($('select[name=select-pays]'));
            //console.log(map);
            for (let i = 0; i < map.length; i++) {
                $(select).append('<option value=' + map[i]["idPays"] + '> ' + map[i]["nom"]);
            }
        },
        error(req) {
            console.log(req);
        }
    });

    $('#ajouterPartenaire').on('click', function(){
        if (verifFormPartenaire($('#formAjouterPartenaire'))){
            let jsonPart = form2Json($('#formAjouterPartenaire'));
            console.log(jsonPart);
            $.ajax({
                url: '/',
                type: 'POST',
                async: false,
                data: {
                    action: 'ajouterPartenaire',
                    info : jsonPart
                },
                success(resp){
                    console.log("true"+resp);
                    $('#notificationAjoutPartenaireSucces').attr('hidden', false);
                    $('#notificationAjoutPartenaireSucces').text('Ajout réussi');
                    location.reload();
                },
                error(req){
                    switch (req.responseText) {
                        case 'adresseMailInvalide':
                            $('#notificationAjoutPartenaireError').attr('hidden', false);
                            $('#notificationAjoutPartenaireError').text('Mail invalide');
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    });
}

//------------------------------------------------------------------------------

chargerScriptAjoutPartenaire();