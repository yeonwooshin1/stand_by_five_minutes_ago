console.log("list js exe")

// [1] pjList 전체 조회
const readAllPj = async () => {
    // [1.1] html 표시 영역
    const pjListTbody = document.querySelector(".pjListTbody")
    try {
        // [1.2] fetch
        const r = await fetch("/project/info")
        const d = await r.json()
        console.log(d)

        // [1.3] html 표시
        let html = '';

        if (d.length != 0) {
            let i = 1
            d.forEach(pjDto => {
            html += `<tr>
                        <td>${i}</td>
                        <td><a href="/project/info.jsp?pjNo=${pjDto.pjNo}">${pjDto.pjName}</td>
                        <td>${pjDto.clientName}</td>
                        <td>${pjDto.clientRepresent}</td>
                        <td>${pjDto.clientPhone}</td>
                        <td>${pjDto.pjStartDate}</td>
                        <td>${pjDto.pjEndDate}</td>
                        <td>${pjDto.updateDate}</td>
                    </tr>`
                    i++
            });
        } else {
            html += `<tr>
                <td colspan="8"> ※ 표시할 정보가 없습니다.</td>
             </tr>`
        }
        pjListTbody.innerHTML = html;
    } catch (error) {
        console.log(error)
    }
} // func end
readAllPj()