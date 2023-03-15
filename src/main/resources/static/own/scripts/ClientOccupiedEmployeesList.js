function getClientOccupiedEmployeesList(clientId, tableCell) {
  console.log('Getting occupied employees count for client with ID:' + clientId);
  $.ajax({
    url: '/client/occupiedEmployeesList/' + clientId,
    type: 'GET',
    success: function(data) {
      var employeeCount = data.length;
      console.log('Occupied employees count:', employeeCount);
      $(tableCell).text(employeeCount);
    },
    error: function(jqXHR, textStatus, errorThrown) {
      console.log('Error getting occupied employees count:', textStatus);
    }
  });
}

$(document).ready(function() {
  $('#DataTables_Table_2 tbody tr').each(function() {
    // get the client ID and employee count cell for this row
    var clientId = $(this).find('td:first').text();
    var tableCell = $(this).find('#employeeCountCell');

    // call the getClientOccupiedEmployeesList function for this client ID and table cell
    getClientOccupiedEmployeesList(clientId, tableCell);
  });
});
