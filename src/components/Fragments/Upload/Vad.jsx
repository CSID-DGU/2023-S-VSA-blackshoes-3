import { useState } from "react";
import * as A from "./UploadStyle";
import PlusButton from "../../../assets/images/plus-button.svg";
import PropTypes from "prop-types";
import UploadAdInput from "../Reusable/AdInput";

const Vad = ({ step, handleAdList }) => {
  // Constant----------------------------------------------------

  // State-------------------------------------------------------
  const [adInputs, setAdInputs] = useState([]);
  const [adIdx, setAdIdx] = useState(0);

  // Function----------------------------------------------------
  const addInput = (e) => {
    e.preventDefault();
    const newInput = {
      id: adIdx,
    };
    setAdIdx(adIdx + 1);
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
              <UploadAdInput
                key={params.id}
                adIdx={params.id}
                adInputs={adInputs}
                setAdInputs={setAdInputs}
                handleAdList={handleAdList}
              />
            ))}
      </A.AdUploadGridBox>
    </A.AdUploadSection>
  );
};

export default Vad;

Vad.propTypes = {
  step: PropTypes.object,
  handleAdList: PropTypes.func,
};
