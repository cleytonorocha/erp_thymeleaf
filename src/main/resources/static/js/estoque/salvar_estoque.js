document.addEventListener('DOMContentLoaded', async function () {
    document.getElementById('estoqueForm').addEventListener('submit', async function (event) {
        event.preventDefault()
        const formData = new FormData(this)
        const api = '/api/estoque'
        const metodo = 'POST'

        try {
            const response = await fetch(api, {
                method: metodo,
                body: formData
            })

            if (response.ok || response.status === 201) {
                const data = await response.text()
                console.log('Sucesso:', data)
                alert('Formulário enviado com sucesso!')
            } else {
                let errorMessage = 'Erro desconhecido'
                try {
                    const errorData = await response.json()
                    console.log(errorData)

                    if (errorData && typeof errorData === 'object') {
                        const errorMessages = Object.values(errorData)
                        if (errorMessages.length > 0) {
                            errorMessage = errorMessages[1]
                        }
                    }
                } catch (jsonError) {
                    errorMessage = await response.text()
                }

                alert(`Erro ao enviar o formulário: ${errorMessage}`)
            }
        } catch (error) {
            alert('Ocorreu um erro ao enviar o formulário. Por favor, tente novamente.')
        }
    })
})


function previewImagem(event) {
    const imagem = event.target.files[0];
    const preview = document.getElementById('imagemPreview');

    if (imagem) {
        const reader = new FileReader();
        reader.onload = function (e) {
            preview.src = e.target.result;
            preview.style.display = 'block';
        };
        reader.readAsDataURL(imagem);
    } else {
        preview.src = '';
        preview.style.display = 'none';
    }
}

function limparPreview() {
    const preview = document.getElementById('imagemPreview');
    const inputImagem = document.getElementById('imagem');

    preview.src = '';
    preview.style.display = 'none';
    inputImagem.value = '';
}