import { useState } from "react";
import AdInput from "../Reusable/AdInput";
import * as A from "./UploadStyle";
import PlusButton from "../../../assets/images/plus-button.svg";
import PropTypes from "prop-types";

const Vad = ({ step, setStartTime, setEndTime, setAdContent, setAdUrl }) => {
  // Constant----------------------------------------------------
  const defaultInput = {
    id: Date.now(),
  };

  // State-------------------------------------------------------
  const [adInputs, setAdInputs] = useState([defaultInput]);

  // Function----------------------------------------------------
  const addInput = (e) => {
    e.preventDefault();
    const newInput = {
      id: Date.now(),
    };
    setAdInputs([...adInputs, newInput]);
  };

  return (
    <A.AdUploadSection>
      {/* {step.second && <Shadow>STEP 2</Shadow>} */}
      <A.TitleLeftBox>
        <A.SpanTitle>광고등록</A.SpanTitle>
        <A.AdUploadButton onClick={addInput}>
          <A.FullIcon src={PlusButton} alt="plus-button" loading="lazy" />
        </A.AdUploadButton>
      </A.TitleLeftBox>
      <A.AdUploadGridBox>
        {adInputs.length === 0
          ? "광고를 추가해주세요."
          : adInputs.map((params) => (
              <AdInput
                key={params.id}
                adId={params.id}
                adInputs={adInputs}
                setAdInputs={setAdInputs}
                setStartTime={setStartTime}
                setEndTime={setEndTime}
                setAdContent={setAdContent}
                setAdUrl={setAdUrl}
              />
            ))}
      </A.AdUploadGridBox>
    </A.AdUploadSection>
  );
};

export default Vad;

Vad.propTypes = {
  step: PropTypes.object,
  setStartTime: PropTypes.func,
  setEndTime: PropTypes.func,
  setAdContent: PropTypes.func,
  setAdUrl: PropTypes.func,
};
