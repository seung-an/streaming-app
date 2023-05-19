import styles from "styles/common/Modal.module.css";
import { useRef } from "react";

function Modal({ open, close, title, children }) {
  const outside = useRef();

  return (
    // 모달이 열릴때 openModal 클래스가 생성된다.
    <div
      className={open ? `${styles.openModal} ${styles.modal}` : styles.modal}
      ref={outside}
      onClick={(e) => {
        if (e.target == outside.current) close();
      }}
    >
      {open ? (
        <section>
          <header>
            {title}
            <button onClick={close}>&times;</button>
          </header>
          <main>{children}</main>
          <footer>
            <button onClick={close}>닫기</button>
          </footer>
        </section>
      ) : null}
    </div>
  );
}

export default Modal;
