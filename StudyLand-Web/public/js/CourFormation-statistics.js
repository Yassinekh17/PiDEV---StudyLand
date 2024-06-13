fetch('/cour-statistics') // Update the endpoint to fetch course statistics
    .then(response => response.json())
    .then(data => {
        // Render chart using Chart.js
        const ctx = document.getElementById('courChart').getContext('2d');
        const courChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: data.labels,
                datasets: [{
                    label: 'Number of Courses',
                    data: data.data,
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    })
    .catch(error => console.error('Error fetching course statistics:', error));
