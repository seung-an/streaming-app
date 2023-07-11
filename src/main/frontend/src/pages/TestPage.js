import { api, authApi } from "api/api";
import { useEffect, useState } from "react";

function TestPage() {
  const [result, setResult] = useState([]);

  const testApi = async () => {
    await authApi.get("/api/playlist/getPlaylist").then((response) => {
      console.log(response.data.data);
    });
  };

  useEffect(() => {}, []);
  return (
    <div>
      <button onClick={testApi}>조회</button>
    </div>
  );
}

export default TestPage;
