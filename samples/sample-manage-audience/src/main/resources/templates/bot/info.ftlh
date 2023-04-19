<#import "../__wrapper.ftlh" as wrapper>
<@wrapper.main>

    <h1>Bot information</h1>

    <table class="table table-bordered">
        <tr>
            <th>userId</th>
            <td>${botInfo.userId}</td>
        </tr>
        <tr>
            <th>basicId</th>
            <td>${botInfo.basicId}</td>
        </tr>
        <tr>
            <th>premiumId</th>
            <td>${botInfo.premiumId!"-"}</td>
        </tr>
        <tr>
            <th>displayName</th>
            <td>${botInfo.displayName}</td>
        </tr>
        <tr>
            <th>pictureUrl</th>
            <td>${botInfo.pictureUrl}<br>
                <img src="${botInfo.pictureUrl}" alt="pictureUrl"></td>
        </tr>
        <tr>
            <th>chatMode</th>
            <td>${botInfo.chatMode}</td>
        </tr>
        <tr>
            <th>markAsReadMode</th>
            <td>${botInfo.markAsReadMode}</td>
        </tr>
    </table>

</@wrapper.main>
