import { api, authApi } from "api/api";
import { useState } from "react";

function TestPage() {
  const [test, setTest] = useState("");
  const testApi = () => {
    authApi.get("/api/hello").then((response) => {
      setTest(response.data);
    });
  };
  return (
    <div>
      <div>테스트 결과 : {test}</div>
      <button onClick={testApi}>테스트 버튼</button>
    </div>
  );
}

export default TestPage;
