function getClientOccupiedEmployeesList(clientId, targetElement) {
  console.log('Getting occupied employees count for client with ID:' + clientId);
  $.ajax({
    url: '/client/occupiedEmployeesList/' + clientId,
    type: 'GET',
    success: function(data) {
      var employeeCount = data.length;
      console.log('Occupied employees count:', employeeCount);
      $(targetElement).text(employeeCount);
    },
    error: function(jqXHR, textStatus, errorThrown) {
      console.log('Error getting occupied employees count:', textStatus);
    }
  });
}

$(document).ready(function() {
  var targetElement = '#target';
  var clientId = $('#clientId').val();
  getClientOccupiedEmployeesList(clientId, targetElement);
});
