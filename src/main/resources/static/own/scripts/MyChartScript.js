
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
                    label: 'Брой Проекти',
                    data: numProjects,
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1,
                    borderRadius: 5, // adjust this value to change the radius of the rounded corners
                    barThickness: 40 // adjust this value to change the bar width
                }]
            },
            options: {
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero: true,
                            stepSize: 1,
                callback: function(value, index, values) {
                  if (Math.floor(value) === value) {
                    return value;
                  }
                }
                        }
                    }]
                },
                plugins: {
                    ovalBars: {
                        ratio: 0.5 // adjust this value to change the oval shape
                    }
                }
            }
        });
    });
