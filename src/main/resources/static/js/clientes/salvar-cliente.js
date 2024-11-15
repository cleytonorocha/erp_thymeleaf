document.addEventListener('DOMContentLoaded', async function () {
    document.getElementById('clientForm').addEventListener('submit', async function (event) {
        event.preventDefault()

        const nome = document.querySelector('#nome').value.trim()
        const cpfOrCnpj = document.querySelector('#cpf-or-cnpj').value.trim()
        const numeroContato = document.querySelector('#numero').value.trim()
        const cep = document.querySelector('#cep').value.trim()
        const endereco = document.querySelector('#rua').value.trim()
        const bairro = document.querySelector('#bairro').value.trim()
        const cidade = document.querySelector('#cidade').value.trim()
        const uf = document.getElementById('estado').value
        const numeroCasa = parseInt(document.querySelector('#numero-endereco').value.trim(), 10) || 0

        const identificacao = cpfOrCnpj.replace(/\D/g, '')

        const formData = { nome, identificacao, numeroContato, cep, endereco, bairro, cidade, uf, numeroCasa };
        const api = '/api/cliente'
        const metodo = 'POST'

        const alertPlaceholder = document.getElementById('liveAlertPlaceholder')
        const appendAlert = (message, type) => {
            const wrapper = document.createElement('div')
            wrapper.innerHTML = [
                `<div class="alert alert-${type} alert-dismissible" role="alert">`,
                `   <div>${message}</div>`,
                '   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>',
                '</div>'
            ].join('')

            alertPlaceholder.append(wrapper)
        }

        try {
            const response = await fetch(api, {
                method: metodo,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData)
            })

            if (response.ok || response.status === 201) {
                const data = await response.text()
                appendAlert(data, 'success')
            } else {
                let errorMessage = 'Erro desconhecido'
                try {
                    const errorData = await response.json()
                    for(let i in errorData.error){
                        appendAlert(errorData.error[i], 'danger')
                    }

                    if (errorData && typeof errorData === 'object') {
                        const errorMessages = Object.values(errorData)
                        if (errorMessages.length > 0) {
                            errorMessage = errorMessages[0]
                        }
                    }

                } catch (jsonError) {
                    errorData = await response.text()
                }
            }
        } catch (error) {
            console.error('Erro na requisição:', error)
            appendAlert(data, 'danger')
        }
    })
})


