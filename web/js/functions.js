
function checkData(evt) {
  let ok = true;
  let errorDni = document.getElementById("dniError");
  let errorEmail = document.getElementById("emailError");
  let errorPhone = document.getElementById("phoneError");

  let dni = document.getElementById("dni");
  let email = document.getElementById("email");
  let phone = document.getElementById("phone");

  let dniRegex = /^[0-9]{8}[A-Za-z]$/;
  if (!dniRegex.test(dni.value)) {
    errorDni.textContent = "Debes introducir un DNI correcto";
    dni.setAttribute("class", "form-control error");
    evt.preventDefault();
    ok = false;
  } else {
    dni.setAttribute("class", "form-control ok");
    errorDni.textContent = "";
  }

  let emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
  if (!emailRegex.test(email.value)) {
    errorEmail.textContent = "Debes introducir un correo electrónico válido";
    email.setAttribute("class", "form-control error");
    evt.preventDefault();
    ok = false;
  } else {
    email.setAttribute("class", "form-control ok");
    errorEmail.textContent = "";
  }

  let phoneRegex = /^[0-9]{9,15}$/;
  if (!phoneRegex.test(phone.value)) {
    errorPhone.textContent = "Debes introducir un teléfono correcto";
    phone.setAttribute("class", "form-control error");
    evt.preventDefault();
    ok = false;
  } else {
    phone.setAttribute("class", "form-control ok");
    errorPhone.textContent = "";
  }

  return ok;
}
      