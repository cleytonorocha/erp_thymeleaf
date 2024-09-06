document.addEventListener('DOMContentLoaded', () => {
    const cpfRadio = document.getElementById('cpf')
    const cnpjRadio = document.getElementById('cnpj')
    const cpfOrCnpjInput = document.getElementById('cpf-or-cnpj')
    const cpfCnpjLabel = document.getElementById('label-cpf-cnpj')
    const numeroParaContato = document.getElementById('numero')
    const cepInput = document.getElementById('cep')
    const ruaInput = document.getElementById('rua')
    const numeroEnderecoInput = document.getElementById('numero-endereco')
    const bairroInput = document.getElementById('bairro')
    const cidadeInput = document.getElementById('cidade')
    const estadoInput = document.getElementById('estado')

    function updateFields() {
        if (cpfRadio.checked) {
            cpfOrCnpjInput.placeholder = 'Digite o CPF'
            cpfCnpjLabel.textContent = 'CPF'
        } else if (cnpjRadio.checked) {
            cpfOrCnpjInput.placeholder = 'Digite o CNPJ'
            cpfCnpjLabel.textContent = 'CNPJ'
        }
        cpfOrCnpjInput.value = formatCpfOrCnpj(cpfOrCnpjInput.value)
    }

    function formatPhoneNumber(value) {
        const cleaned = value.replace(/\D/g, '');

        if (cleaned.length === 10) {
            return cleaned
                .replace(/^(\d{2})(\d{4})(\d{4})$/, '$1 $2-$3')
                .slice(0, 13);
        } else if (cleaned.length === 11 ) {
            return cleaned
                .replace(/^(\d{2})(\d{5})(\d{4})$/, '$1 $2-$3')
                .slice(0, 14);
        }

        return value;
    }


    function formatCep(value) {
        return value
            .replace(/\D/g, '')
            .replace(/^(\d{5})(\d{3})$/, '$1-$2')
            .slice(0, 9)
    }

    async function fetchAddress(cep) {
        try {
            const response = await fetch(`https://brasilapi.com.br/api/cep/v1/${cep}`);
            if (!response.ok) {
                throw new Error(`Network response was not ok: ${response.statusText}`);
            }

            const data = await response.json();

            if (data) {
                ruaInput.value = data.street || '';
                bairroInput.value = data.neighborhood || '';
                cidadeInput.value = data.city || '';
                estadoInput.value = data.state || ''

                numeroEnderecoInput.focus();
            }
        } catch (error) {
            console.error('Erro ao buscar o endereço:', error);
        }
    }

    cepInput.addEventListener('input', () => {
        cepInput.value = formatCep(cepInput.value)

        if (cepInput.value.length === 9) {
            const cep = cepInput.value.replace('-', '')
            fetchAddress(cep)
        }
    })

    cpfRadio.addEventListener('change', updateFields)
    cnpjRadio.addEventListener('change', updateFields)

    cpfOrCnpjInput.addEventListener('input', () => {
        cpfOrCnpjInput.value = formatCpfOrCnpj(cpfOrCnpjInput.value)
    })

    numeroParaContato.addEventListener('input', () => {
        numeroParaContato.value = formatPhoneNumber(numeroParaContato.value)
    })

    numeroParaContato.setAttribute('maxlength', '13')

    updateFields()
})
