function remplireCard(info){
    let card = $('#rowCardPartenaire'); 
    for (let index = 0; index < info.length; index++) {
        let newElement=$('#rowCardPartenaire').append($('#patternCardPartenaire').html());
        let current = $(newElement).children()[index];
        $(current).find($('input[name=adresse]')).val(info[index]['adresse']);
        $(current).find($('input[name=numTelephone]')).val(info[index]['numTel']);
        $(current).find($('input[name=typeSejour]')).val(info[index]['typeMobilite']);
        if(info[index]['departement']!=""){
            $(current).find($('input[name=departement]')).val(info[index]['departement']);
        }else{
            $(current).find($('input[name=departement]')).val("non défini");
        }
        $(current).find($('input[name=nomEntreprise]')).val(info[index]['nomLegal']);
        $(current).find($('input[name=nomEntreprise]')).parent().parent().parent().parent().attr("id", info[index]['nomLegal'].replace(/\s/g,''));
        $(current).find($('input[name=ville]')).val(info[index]['ville']);
        $.ajax({
            url:'/',
            type:'POST',
            data: {
                action:'getPaysById',
                idPays:info[index]['pays']
            },
            success(resp){
                $(current).find($('input[name=pays]')).val(JSON.parse(resp).nom);
            },
            error(req){
                console.log(req);
            }
        });
        $(current).find($('input[name=cp]')).val(info[index]['codePostal']);
    }
}

function chargerScriptPartenaire(){
    $.ajax({
        url:'/',
        type:'POST',
        async:false,
        data: {
            action:'getAllPartenaire'
        },
        success(resp){
            let map = JSON.parse(resp);
            remplireCard(map);
        },
        error(req){
            console.log(req);
        }
    });

    $('[name=redirectionAjoutPartenaire]').on('click', function(){
        setDernierePage("pageAjoutPartenaire");
        displayPage($('#pageAjoutPartenaire'));
    });
}

//------------------------------------------------------------------------------

chargerScriptPartenaire();