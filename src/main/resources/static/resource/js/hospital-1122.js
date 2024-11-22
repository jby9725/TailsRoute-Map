$(document).ready(function () {
    // 페이지 로드 시 시 데이터를 서버에서 가져옴
    loadCities();
});

// 시 데이터 로드
function loadCities() {
    $.ajax({
        url: '/cities', method: 'GET', success: function (cities) {
            const citySelect = document.getElementById('city-select');
            cities.forEach(city => {
                const option = document.createElement('option');
                option.value = city;
                option.text = city;
                citySelect.appendChild(option);
            });
        }
    });
}

// 선택한 시에 맞는 군/구 데이터를 로드
function loadCounties() {
    const selectedCity = document.getElementById('city-select').value;
    const countySelect = document.getElementById('county-select');

    if (!selectedCity) {
        countySelect.innerHTML = '<option value="">시/군/구 선택</option>';
        return;  // 시가 선택되지 않았으면 실행하지 않음
    }


    // 세종특별자치시 처리 (군/구 선택 없음)
    if (selectedCity === "세종특별자치시") {
        countySelect.innerHTML = '<option value="">시/군/구 없음</option>';
        countySelect.disabled = true;  // 군/구 선택 비활성화
    } else {
        countySelect.disabled = false;
        $.ajax({
            url: '/counties', method: 'GET', data: {city: selectedCity}, success: function (counties) {
                countySelect.innerHTML = '<option value="">시/군/구 선택</option>';  // 기존 군 데이터 초기화

                counties.forEach(county => {
                    const option = document.createElement('option');
                    option.value = county;
                    option.text = county;
                    countySelect.appendChild(option);
                });
            }
        });
    }
}


// 전역 변수로 선언된 map이 이미 존재할 경우 중복 선언하지 않음
if (typeof map === 'undefined') {
    var map;  // let 대신 var로 전역 변수 선언을 수정하여 재선언 방지
}

if (typeof service === 'undefined') {
    var service;
}

// 마커를 저장할 배열 생성
// 기존에 선언된 markers 배열이 있는지 확인
if (typeof markers === 'undefined') {
    var markers = []; // 재선언 방지
}

// 전역 변수로 마커 클러스터 객체 추가
if (typeof markerCluster === 'undefined') {
    var markerCluster;
}

async function initMap() {
    // Google Maps와 Places 라이브러리를 비동기로 불러옴
    const {Map} = await google.maps.importLibrary("maps");

    // 지도 중심 좌표 설정 (서울)
    let center = new google.maps.LatLng(37.5665, 126.9780);

    // 지도를 초기화합니다.
    map = new Map(document.getElementById("map"), {
        center: center, zoom: 14, mapId: google_mapId,
    });

    // 현재 위치를 가져와 지도 중심 설정
    if ("geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition((position) => {
            const {latitude, longitude} = position.coords;
            const userLocation = new google.maps.LatLng(latitude, longitude);
            map.setCenter(userLocation); // 지도의 중심을 현재 위치로 설정
        }, (error) => {
            console.error("현재 위치를 가져올 수 없습니다.", error);
        });
    } else {
        console.warn("Geolocation API를 지원하지 않는 브라우저입니다.");
    }

    // 병원 데이터를 지도에 추가하고 마커 클러스터링 적용
    fetchHospitalsAndUpdateMarkers();

}

function fetchHospitalsAndUpdateMarkers(filterType = "") {
    fetch(`/hospitals?type=${filterType}`)
        .then((response) => response.json())
        .then((data) => {

            console.log("Fetched Data:", data); // 로그 추가

            // 기존 마커 제거
            markers.forEach(marker => marker.setMap(null));
            markers.length = 0;

            // 클러스터링 초기화
            if (markerCluster) {
                markerCluster.clearMarkers();
            }

            // 새로운 마커 추가
            data.forEach((hospital) => {
                if (hospital.latitude && hospital.longitude) {
                    const marker = new google.maps.Marker({
                        position: { lat: parseFloat(hospital.latitude), lng: parseFloat(hospital.longitude) },
                        map: map,
                        title: hospital.name,
                    });
                    markers.push(marker); // 마커 배열에 추가

                    // 클릭 이벤트 추가
                    const infoWindow = new google.maps.InfoWindow({
                        content: `<h3>${hospital.name}</h3><p>주소: ${hospital.roadAddress}</p><p>전화번호: ${hospital.callNumber}</p>`
                    });

                    marker.addListener('click', () => {
                        infoWindow.open(map, marker);
                    });
                }
            });

            // 마커가 존재할 때만 클러스터링 적용
            if (markers.length > 0) {
                markerCluster = new MarkerClusterer(map, markers, {
                    imagePath: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m',
                });
            }
        })
        .catch((error) => console.error("Error fetching hospital data:", error));
}

// 토글 버튼 이벤트로 필터링 적용
document.querySelector('input[role="switch"]').addEventListener("change", function () {
    const isChecked = this.checked;
    const filterType = isChecked ? "24시간" : "";
    fetchHospitalsAndUpdateMarkers(filterType);
});

// 신) 사용자가 시/군/구를 선택하여 주소를 좌표로 변환
function geocodeAddress() {
    const selectedCity = document.getElementById('city-select').value;
    const selectedCounty = document.getElementById('county-select').value;

    if (!selectedCity || (selectedCity !== "세종특별자치시" && !selectedCounty)) {
        alert("시와 군/구를 모두 선택하세요.");
        return;
    }

    const geocoder = new google.maps.Geocoder();
    const address = selectedCity + (selectedCity !== "세종특별자치시" ? " " + selectedCounty : "");

    // 주소 값을 콘솔에 출력하여 확인
    console.log("Geocoding 주소:", address);

    geocoder.geocode({address}, function (results, status) {
        if (status === 'OK') {

            const location = results[0].geometry.location;  // 위치 정보 가져오기

            const lat = location.lat();
            const lng = location.lng();

            if (lat && lng) {
                console.log("Geocoded location (lat, lng):", lat, lng);
                map.setCenter(location);
                map.setZoom(14);  // 줌 설정

                // 동물 병원 검색 (Google Places API 사용)
                // findAnimalHospitals(location);
            } else {
                console.error('위도 및 경도 값이 null입니다.');
            }

        } else {
            alert("주소 변환 실패: " + status);
        }
    });
}

// CSS javascript
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const toggleButton = document.getElementById('toggle-button');

    sidebar.classList.toggle('open');

    // 토글 버튼의 방향 변경
    if (sidebar.classList.contains('open')) {
        toggleButton.innerHTML = '&#9664;'; // 화살표를 오른쪽으로
    } else {
        toggleButton.innerHTML = '&#9654;'; // 화살표를 왼쪽으로
    }
}
