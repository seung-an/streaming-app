import { api, authApi } from "api/api";
import { useEffect, useState } from "react";

function TestPage() {
  const [test, setTest] = useState("");
  const [formData, setFormData] = useState(null);
  const testApi = () => {
    authApi
      .post("/api/file/video/upload", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      })
      .then((response) => {
        setTest(response.data);
      });
  };

  const reset = () => {
    authApi.get("/api/file/multiFileUploadAllStop").then((response) => {
      console.log(response.data);
    });
  };

  const onChangeFile = (event) => {
    if (!event.target.files) {
      return;
    }

    const form = new FormData();
    form.append("file", event.target.files[0]);

    setFormData(form);
  };

  return (
    <div>
      <div>
        <video controls style={{ width: "300px", height: "168.75px" }}>
          <source type="video/mp4" src={test} />
        </video>
      </div>
      <input type="file" id="fileUpload" onChange={onChangeFile} />
      <button onClick={testApi}>업로드 버튼</button>
      <button onClick={reset}>초기화 버튼</button>
    </div>
  );
}

export default TestPage;
