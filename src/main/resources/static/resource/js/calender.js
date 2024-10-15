
document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var events = [/*[[${diaries}]]*/].map(function (diary){
        return {
            title: diary.title,
            start: diary.regDate.replace(' ', 'T') // 공백을 T로 교체
        };
    })

    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth', // 기본 보기를 월간 그리드로 설정
        locale: 'ko', // 한국어 설정
        headerToolbar: {
            right: 'prev,next today',
            center: 'title',
            left: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        events: events,
    });

    calendar.render(); // 캘린더 렌더링
});