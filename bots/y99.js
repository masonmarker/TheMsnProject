

// new partner classname
let new_partner_classname = "btn btn--small theme--dark cyan darken-1 elevation-0";

// chat input textarea classname
let chat_input_textarea_classname = "chatinput input-flex-item private";

// send button classname
let send_button_classname = "input-flex-item nosel";

// message to send
let message = "hi im jennie and im 19! send me a dm on instagram ;))   jennie.maylee";

// entire chat div classname
let chat_div_classname = "input_wrap";

// for every 2 seconds
setInterval(function () {

    // get input textarea element
    let input_textarea = document.getElementsByClassName(chat_input_textarea_classname)[0];

    // get send button element
    let send_button = document.getElementsByClassName(send_button_classname)[0];


    // type the message into the input textarea one character at a time
    for (let i = 0; i < message.length; i++) {
        setTimeout(function () {
            // type the character
            input_textarea.value += message[i];

            // dispatch an input event
            input_textarea.dispatchEvent(new Event('input', { bubbles: true }));
        }, i * 10);
    }



    

    // wait for 1000 ms before pressing the enter key on the textarea
    setTimeout(function () {
        // press enter key on textarea
        input_textarea.dispatchEvent(new KeyboardEvent('keydown', {'key': 'Enter'}));
    }, 3000);



    // wait 500 ms before pressing the enter key on the input wrap
    setTimeout(function () {
        // get input wrap element
        let input_wrap = document.getElementsByClassName(chat_div_classname)[0];
        
        // press enter key on input wrap
        input_wrap.dispatchEvent(new KeyboardEvent('keydown', {'key': 'Enter'}));
    }, 5000);

    // wait 500 ms before clicking new partner button
    setTimeout(function () {
        // get new partner button element
        let new_partner_button = document.getElementsByClassName(new_partner_classname)[0];

        // click the new partner button
        new_partner_button.click();

        // click again
        new_partner_button.click();
    }, 4000);

}, 10000);

