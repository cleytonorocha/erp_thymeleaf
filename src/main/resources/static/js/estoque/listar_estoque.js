document.addEventListener('DOMContentLoaded', function () {
    const url = new URL(window.location.href);
    const params = new URLSearchParams(url.search);
    const pagina = params.get('pagina');

    function loadEstoque(pagina) {
        fetch(`api/estoque?pagina=${pagina - 1}`)
            .then(response => response.json())
            .then(estoque => {
                const container = document.getElementById('estoqueContainer');
                container.innerHTML = '';

                estoque.content.forEach(item => {

                    const card = document.createElement('div');
                    card.className = 'col-12';

                    card.innerHTML = `
                            <div class="mb-4 shadow-lg border rounded p-4">
                            <div class="row g-0">
                                <div class="col-xl-4 shadow" style="height: 30em ">
                                    <img src="${item.pathImagem}" class="img-fluid h-100 w-100 object-fit-cover " alt="${item.nome}">
                                </div>
                            <div class="col-xl-8 border p-5 shadow">
                                <div class="card-body ms-0 ms-lg-5">
                                <h5 class="card-title text-primary mb-3 display-5">${item.nome}</h5>
                                <div class="d-flex justify-content-between align-items-center mt-3">
                                <small class="text-muted">ID: ${item.id}</small>
                                        </div>
                                            <div class="row mb-3 fs-5">
                                                <div class="col-sm-6">
                                                    <p class="card-text"><strong>Categoria:</strong> ${item.categoria.nome}</p>
                                                    <p class="card-text"><strong>Preço Unitário:</strong> R$ ${item.precoUnitario.toFixed(2)}</p>
                                                    <p class="card-text"><strong>Quantidade:</strong> ${item.quantidade}</p>
                                                </div>
                                                <div class="col-sm-6">
                                                    <p class="card-text"><strong>Validade:</strong> ${formatDate(item.validade)}</p>
                                                    <p class="card-text"><strong>Data da Compra:</strong> ${formatDateTime(item.dataDaCompra)}</p>
                                                </div>
                                            </div>
                                            
                                            <div class="row mb-2 fs-5">
                                                <div class="col-sm-6">
                                                    <p class="card-text"><strong>Criado por:</strong> ${item.criadoPor || 'N/A'}</p>
                                                    <p class="card-text"><strong>Criado em:</strong> ${formatDateTime(item.dataCriacao)}</p>
                                                </div>
                                                <div class="col-sm-6">
                                                    <p class="card-text"><strong>Modificado por:</strong> ${item.modificadoPor || 'N/A'}</p>
                                                    <p class="card-text"><strong>Modificado em:</strong> ${formatDateTime(item.dataModificacao)}</p>
                                                </div>
                                            </div>

                                            </div>
                                            <div class="d-flex w-100 mt-3 align-items-end ">
                                                <a class="btn btn-sm btn-outline-danger ms-auto"  onclick="deleteEstoque(${item.id});">
                                                    <i class="fa-solid fa-trash-can"></i> Excluir
                                                </a>
                                            </div>
                                        </div>
                                </div>
                            </div>

                    `;
                    container.appendChild(card);
                });
            })
            .catch(error => {
                alert('Erro ao carregar o estoque: ' + error.message);
            });
    }

    loadEstoque(pagina);
});

function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR', { timeZone: 'UTC' });
}

function formatDateTime(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleString('pt-BR', { timeZone: 'UTC' });
}
function deleteEstoque(id) {
    if (confirm('Deseja realmente deletar o produto?')) {
        fetch(`api/estoque/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            }
        }).then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Falha ao deletar produto.');
            }
        }).catch(error => {
            console.error('Erro:', error);
        });
    }
}