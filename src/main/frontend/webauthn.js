// Code copié collé de https://martinelli.ch/passkeys-vaadin-flow-spring-security/

async function post(url, csrf, body) {
    const response = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', [csrf.headerName]: csrf.token },
        body: body ? JSON.stringify(body) : undefined,
    });
    if (!response.ok) {
        throw new Error(`${url} answered HTTP ${response.status}`);
    }
    return response.json();
}

async function register(csrf, label) {
    const options = await post('webauthn/register/options', csrf);
    const credential = await navigator.credentials.create({
        publicKey: PublicKeyCredential.parseCreationOptionsFromJSON(options),
    });
    const result = await post('webauthn/register', csrf, { publicKey: { label, credential: credential.toJSON() } });
    if (!result.success) {
        throw new Error('The server rejected the passkey');
    }
    return true;
}

async function authenticate(csrf) {
    const options = await post('webauthn/authenticate/options', csrf);
    const credential = await navigator.credentials.get({
        publicKey: PublicKeyCredential.parseRequestOptionsFromJSON(options),
    });
    const result = await post('login/webauthn', csrf, credential.toJSON());
    if (!result.authenticated) {
        throw new Error('Passkey sign-in failed');
    }
    return result.redirectUrl;
}

window.passkeys = { register, authenticate };