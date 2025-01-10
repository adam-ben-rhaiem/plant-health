// Fixed locations of plant care centers in Tunisia
const plantCareCenters = [
    { name: "Nour Agricole", lat: 36.8501, lon: 10.1803, address: "Tunis City Center", phone: "71254277" },
    { name: "UTAP Tunisie", lat: 36.8383, lon: 10.1886, address: "Tunis City Center", phone: "71806800" },
    { name: "Coopérative Centrale des Semences", lat: 36.8793, lon: 10.2387, address: "Tunis City Center", phone: "12345678" },
    { name: "Société Equipement Agricole Tunisie S.E.A.T", lat: 36.803970349700585, lon:10.18515124235817, address: "27 Rue Massoudi - Cité Fateh, Tunis 2036", phone: "71240156" },
    { name: "Société Mutuelle de Services Agricoles El Bassatine Soukra", lat: 36.89550345019001,lon: 10.256163905198703, address: "Medinine City Center", phone: "22519862" },
  ];

const defaultLocation = [33.8869, 9.5375]; // Default location: Tunisia
let map;
let userMarker;

// Initialize the map
function initMap(latitude, longitude) {
  map = L.map('map').setView([latitude, longitude], 8);

  // Add OpenStreetMap tiles
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
  }).addTo(map);

  // Add marker for user's location
  userMarker = L.marker([latitude, longitude]).addTo(map).bindPopup('You are here!').openPopup();

  // Find and display the nearest center
  findNearestPlantCareCenter(latitude, longitude);
}

// Find the nearest plant care center from the fixed list
function findNearestPlantCareCenter(latitude, longitude) {
  let nearestCenter = null;
  let shortestDistance = Infinity;

  plantCareCenters.forEach(center => {
    const distance = getDistanceFromLatLonInKm(latitude, longitude, center.lat, center.lon);
    if (distance < shortestDistance) {
      shortestDistance = distance;
      nearestCenter = center;
    }
  });

  if (nearestCenter) {
    // Add marker for the nearest center
    L.marker([nearestCenter.lat, nearestCenter.lon])
      .addTo(map)
      .bindPopup(`${nearestCenter.name}<br>${nearestCenter.address}`)
      .openPopup();

    // Update the info section
    document.getElementById('center-name').textContent = nearestCenter.name;
    document.getElementById('center-address').textContent = `Address: ${nearestCenter.address}`;
    document.getElementById('center-phone').textContent = `Phone: ${nearestCenter.phone || "N/A"}`;
    document.getElementById('center-distance').textContent = `Distance: ${shortestDistance.toFixed(2)} km`;
  }
}

// Get the user's location
function getUserLocation() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const { latitude, longitude } = position.coords;

        // Update message
        document.getElementById('location-message').textContent = 'Location found. Searching for centers...';

        // Initialize the map
        initMap(latitude, longitude);
      },
      () => {
        document.getElementById('location-message').textContent = 'Unable to access location. Please enable location access.';
        initMap(defaultLocation[0], defaultLocation[1]); // Use default location
      }
    );
  } else {
    document.getElementById('location-message').textContent = 'Geolocation is not supported by your browser.';
    initMap(defaultLocation[0], defaultLocation[1]); // Use default location
  }
}

// Helper function to calculate distance between two coordinates (Haversine formula)
function getDistanceFromLatLonInKm(lat1, lon1, lat2, lon2) {
  const R = 6371; // Radius of the Earth in km
  const dLat = deg2rad(lat2 - lat1);
  const dLon = deg2rad(lon2 - lon1);
  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
    Math.sin(dLon / 2) * Math.sin(dLon / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return R * c; // Distance in km
}

function deg2rad(deg) {
  return deg * (Math.PI / 180);
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', () => {
  getUserLocation();
});
