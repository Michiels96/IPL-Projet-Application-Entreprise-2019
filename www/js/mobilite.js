//Creation des cartes avec les bonnes données
function initialisation(mobilites) {
    if (mobilites.length > 0) {
        console.log(mobilites);
        for (let i = 0; i < mobilites.length; i++) {
            let map = {};
            map["documentsDepart"] = mobilites[i]["documentsDepart"];
            map["documentsRetour"] = mobilites[i]["documentsRetour"];
            map["priorite"] = mobilites[i]["niveauPreference"];
            map["idMobilite"] = mobilites[i]["idMobilite"];
            map["partenaire"] = mobilites[i]["partenaire"];
            map["periode"] = mobilites[i]["periode"];
            map["typeMobilite"] = mobilites[i]["typeMobilite"];
            map["typeProgramme"] = mobilites[i]["programme"];
            map["idPays"] = mobilites[i]["pays"];
            map["numVersion"] = mobilites[i]["version"];
            map["mobiliteAnnule"] = mobilites[i]["mobiliteAnnule"];
            if (map["partenaire"] != 0) {
                $.ajax({ //Get le pays 
                    url: '/',
                    async: false,
                    type: 'POST',
                    data: {
                        action: 'getPaysById',
                        idPays: mobilites[i]["pays"]
                    },
                    success(resp) {
                        let ret = JSON.parse(resp);
                        map["idPays"] = ret["idPays"];
                        map["typeProgramme"] = ret["programme"];
                    },
                    error(req) {
                        console.log(req);
                    }
                });
                $.ajax({
                    url: '/',
                    async: false,
                    type: 'POST',
                    data: {
                        action: 'getPartenaireById',
                        idPartenaire: mobilites[i]["partenaire"]
                    },
                    success(resp) {
                        let ret = JSON.parse(resp);
                        map["nomComplet"] = ret["nomComplet"];
                        map["typeMobilite"] = ret["typeMobilite"];
                    },
                    error(req) {
                        console.log(req);
                    }
                });
            }
            creatCardAvecDonnee(map, i);
        }
    } else {
        creatCard();
    }
}

function creatCardAvecDonnee(mobilite, indice) {
    let newElement = $('#rowCard').append($('#patternCard').html());
    gestionPriorite();
    let current = $(newElement).children()[indice];
    $(current).attr('idMobilite', mobilite['idMobilite']);
    $(current).attr('numVersion', mobilite['numVersion']);
    $(current).attr('documentsDepart', mobilite["documentsDepart"]);
    $(current).attr('documentsRetour', mobilite["documentsRetour"]);
    $(current).find($('select[name=priorite]')).find($('option[value=' + mobilite["priorite"] + ']')).attr('selected', 'selected');
    $(current).find($('input[name=input-typeMobilite]')).val(mobilite["typeProgramme"]);
    console.log(mobilite.mobiliteAnnule);
    if (mobilite.documentsDepart != 0 && mobilite.mobiliteAnnule==false) {
        $(current).find($('button[name=btn-x]')).text("Annuler mobilité");
        $(current).find($('button[name=btn-x]')).attr('class','btn btn-danger');
    }else if(mobilite.documentsDepart != 0 && mobilite.mobiliteAnnule==true){
        $(current).find($('button[name=btn-x]')).text("Mobilité annulée");
        $(current).find($('button[name=btn-x]')).attr('class','btn btn-danger');
        $(current).find($('button[name=btn-x]')).attr('disabled',true);
    }
    if (mobilite["typeMobilite"] === "SMS") {
        $(current).find($('input[value=sms]')).attr('checked', 'checked');
        getInfoPartenaireOnChangeSms($(current), "sms");
    } else {
        $(current).find($('input[value=smp]')).attr('checked', 'checked');
        getInfoPartenaireOnChangeSms($(current), "smp");
    }
    if (mobilite["periode"] === "Q2") {
        $(current).find($('input[value="2quad"]')).attr('checked', 'checked');
    } else {
        $(current).find($('input[value="1quad"]')).attr('checked', 'checked');
    }
    $(current).find($('select[name=select-pays]')).find($('option[value=' + mobilite["idPays"] + ']')).attr('selected', 'selected');
    $(current).find($('select[name=select-partenaire]')).find($('option[value=' + mobilite["partenaire"] + ']')).attr('selected', 'selected');
}

function creatCard() {
    $('#rowCard').append($('#patternCard').html());
    let current = $('#rowCard').children()[$('#rowCard').children().length - 1];
    $(current).find($('select[name=select-partenaire]')).empty();
    getInfoPartenaireOnChangeSms($(current), "sms");
    gestionPriorite();
}

//Adapte la carte au type SMS
function changeSMS(element) {
    console.log($(element));
    getInfoPartenaireOnChangeSms($(element).parent().parent().parent().parent().parent().parent().parent().parent().parent(), "sms");
}

//Adapte la carte au type SMP
function changeSMP(element) {
    getInfoPartenaireOnChangeSms($(element).parent().parent().parent().parent().parent().parent().parent().parent().parent(), "smp");
}

//Supprime la carte (bouton X)
function removeCard(element) {

    if ($(element).parent().parent().parent().parent().parent().attr('documentsDepart') != 0) {
        $.ajax({
            url: '/',
            type: 'POST',
            data: {
                action: 'annulerMobilite',
                id: $(element).parent().parent().parent().parent().parent().attr('idMobilite')
            },
            success(resp) {
                $(element).parent().parent().parent().parent().parent().find($('button[name=btn-x]')).text("Mobilité annulée");
                $(element).parent().parent().parent().parent().parent().find($('button[name=btn-x]')).attr('class','btn btn-danger');
                $(element).parent().parent().parent().parent().parent().find($('button[name=btn-x]')).attr('disabled',true);
            },
            error(req) {
            }
        });
    } else {
        if ($('div.card').length > 2) {
            $(element).parent().parent().parent().parent().remove();
        }
        $('[name=priorite]').each(function (indice, element) {
            $(element).find('option:last').remove();
        });
    }

}

function getInfoPartenaireOnChangeSms(element, type) {
    $.ajax({
        url: '/',
        type: 'POST',
        async: false,
        data: {
            action: 'getAllPartenaire'
        },
        success(resp) {
            let map = JSON.parse(resp);
            if (type === "sms") {
                remplireSelectPartenaireOnChangeSms(map, element);
            } else {
                remplireSelectPartenaireOnChangeSmp(map, element);
            }
        },
        error(req) {
            console.log(req);
        }
    });
}


function remplireSelectPartenaire(map) {
    let select = $('select[name=select-partenaire]');
    for (let i = 0; i < map.length; i++) {
        if (map[i]['typeMobilite'] === "SMS") {
            $(select).append('<option value="' + map[i]["idPartenaire"] + '">' + map[i]["nomComplet"]);
        }
    }
}

function remplireSelectPartenaireOnChangeSms(map, element) {
    let select = $(element).find($('select[name=select-partenaire]'));
    $(select).empty();
    $(select).append('<option value="0">Choose...');
    for (let i = 0; i < map.length; i++) {
        if (map[i]['typeMobilite'] === "SMS") {
            $(select).append('<option value="' + map[i]["idPartenaire"] + '">' + map[i]["nomComplet"]);
        }
    }
}
function remplireSelectPartenaireOnChangeSmp(map, element) {
    let select = $(element).find($('select[name=select-partenaire]'));
    $(select).empty();
    $(select).append('<option value="0">Choose...');
    for (let i = 0; i < map.length; i++) {
        if (map[i]['typeMobilite'] === "SMP") {
            $(select).append('<option value="' + map[i]["idPartenaire"] + '">' + map[i]["nomComplet"]);
        }
    }
}

function changeSelect(element) {
    $.ajax({
        url: '/',
        type: 'POST',
        data: {
            action: 'getPartenaireById',
            idPartenaire: $(element).val()
        },
        success(resp) {
            let json = JSON.parse(resp);
            $.ajax({
                url: '/',
                type: 'POST',
                data: {
                    action: 'getPaysById',
                    idPays: json["pays"]
                },
                success(resp) {
                    changeTypeMobilite(JSON.parse(resp), element);
                },
                error(req) {
                    console.log(req);
                }
            });
        },
        error(req) {
            console.log(req);
        }
    });
}

function changeTypeMobilite(rep, element) {
    console.log(rep);
    $(element).parent().parent().find('input[name=input-typeMobilite]').val(rep["programme"]);
    $(element).parent().parent().find('select[name=select-pays]').find('option[value=' + rep["idPays"] + ']').attr('selected', 'selected');
}

function enregistrerMobilite() {
    let array = [];
    $('form[name=form-mobilite]').each(function (index, element) {
        let map = {};
        if (index > 0) {
            map["idMobilite"] = $(element).parent().parent().parent().parent().parent().attr('idMobilite');
            map["numVersion"] = $(element).parent().parent().parent().parent().parent().attr('numVersion');
            $(element).find('select').each(function (idx, elem) {
                console.log($(elem).attr("name") + $(elem).find('option:selected').val());
                map[$(elem).attr("name")] = $(elem).find('option:selected').val();
            });
            $(element).find('input').each(function (idx, elem) {
                if ($(elem).attr("type") === "radio") {
                    if ($(elem).is(':checked')) {
                        map[$(elem).attr("name")] = $(elem).val();
                        console.log($(elem).attr("name") + " " + $(elem).val());
                    }
                } else {
                    map[$(elem).attr("name")] = $(elem).val();
                    console.log($(elem).attr("name") + " " + $(elem).val());
                }
            });
            array.push(map);
        }
    });
    console.log(JSON.stringify(array));
    $.ajax({
        url: '/',
        type: 'POST',
        data: {
            action: 'enregistrerMobilite',
            mobilites: JSON.stringify(array)
        },
        success(rep) {
            console.log(rep);
        },
        error(req) {
            console.log(req);
        }
    });
}

function onChangePays(current) {
    $.ajax({
        url: '/',
        type: 'POST',
        data: {
            action: 'getPaysById',
            idPays: $(current).find('option:selected').val()
        },
        success(resp) {
            let ret = JSON.parse(resp);
            $(current).parent().parent().find('input[name=input-typeMobilite]').val(ret["programme"]);
            $(current).parent().parent().parent().find('[name=select-partenaire]').find('option:first').prop('selected', 'selected');
        },
        error(req) {
            console.log(req);
        }
    });
}

function gestionPriorite() {
    let previous = $('[name=priorite]').find('option:last').val();
    previous++;
    $('[name=priorite]').each(function (indice, element) {
        $(element).append('<option value=' + previous + '>' + previous);
    });
}

function chargerScriptMobilite(){
    $.ajax({
        url: '/',
        type: 'POST',
        async: false,
        data: {
            action: 'getAllPartenaire'
        },
        success(resp) { 
            let map = JSON.parse(resp);
            remplireSelectPartenaire(map);
        },
        error(req) {
            console.log(req);
        }
    });

    $.ajax({
        url: '/',
        type: 'POST',
        async: false,
        data: {
            action: 'getAllPays'
        },
        success(resp) {
            let map = JSON.parse(resp);
            let select = $('#patternCard').find($('select[name=select-pays]'));
            for (let i = 0; i < map.length; i++) {
                $(select).append('<option value=' + map[i]["idPays"] + '> ' + map[i]["nom"]);
            }

        },
        error(req) {
            console.log(req);
        }
    });
    //Initialisation des mobilités
    $.ajax({
        url: '/',
        type: 'POST',
        data: {
            action: 'getMobiliteByPseudo',
        },
        success(resp) {
            let mobilites = JSON.parse(resp);
            initialisation(mobilites);
        },
        error(req) {
            console.log(req);
        }
    });
}

//------------------------------------------------------------------------------

chargerScriptMobilite();