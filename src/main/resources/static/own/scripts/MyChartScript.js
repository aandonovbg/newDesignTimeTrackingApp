fetch('/client/allClientsData')
    .then(response => response.json())
    .then(data => {
        const clientNames = [];
        const numProjects = [];
        data.forEach(clientData => {
            clientNames.push(clientData.clientName);
            numProjects.push(clientData.numProjects);
        });

        const clientsChart = new Chart(document.getElementById('clientsChart'), {
            type: 'bar',
            data: {
                labels: clientNames,
                datasets: [{
                    label: 'Number of Projects',
                    data: numProjects,
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero: true
                        }
                    }]
                }
            }
        });
    });
