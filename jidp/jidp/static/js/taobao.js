$(document).ready( function() {
    let brandspin = $( "#brandspin" ).spinner();
    let productspin = $( "#productspin" ).spinner();
    let categoryspin = $( "#categoryspin" ).spinner();
    let cspin1 = $( "#customer1" ).spinner();
    let cspin2 = $( "#customer2" ).spinner();
    let cspin3 = $( "#customer3" ).spinner();
    $( "#rainthreshold" ).slider({
        range: "max",
        min: 6,
        max: 18,
        value: 1,
        slide: function( event, ui ) {
          $( "#currbar" ).val( ui.value );
        }
      });
    $( "#currbar" ).val( $( "#rainthreshold" ).slider( "value" ) );
    $('#submit1').click();
    $('#submit2').click();
} );
