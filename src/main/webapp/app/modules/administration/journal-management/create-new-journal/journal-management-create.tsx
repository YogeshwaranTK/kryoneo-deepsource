import React, {useEffect, useState} from 'react';
import '../journal-management-list/journal-management.scss';
import Breadcrumb from "app/shared/breadcrumb/breadcrumb";
import CreateJournalStep1 from "app/modules/administration/journal-management/create-new-journal/create-journal-step1";
import CreateJournalStep2 from "app/modules/administration/journal-management/create-new-journal/create-journal-step2";
import CreateJournalStep3 from "app/modules/administration/journal-management/create-new-journal/create-journal-step3";
import {useAppDispatch, useAppSelector} from "app/config/store";
import {useNavigate} from "react-router-dom";
import {Storage, Translate, translate} from "react-jhipster";
import {string} from "yup";
import {
  postNewJournal
} from "app/modules/administration/journal-management/journal-management-list/journal-management.reducer";

export const CreateNewJournal = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const [stepActivate, setStepActivate] = useState({stepOne: false, stepTwo: false, stepThree: false});
  const [currentStep, setCurrentStep] = useState(1);
  const [stepOneData, setStepOneData] = useState({});
  const [stepTwoData, setStepTwoData] = useState({
    file: null, summary: string, fileEndPoint: null
  });
  const [stepThreeData, setStepThreeData] = useState({
    articleSubmissionFormats: [],
    articleSubmissionLanguages: [],
    categories: [],
    guidelines: '',
    fileTypes: []
  });

  const JournalDetails = useAppSelector(state => state.journalManagement.JournalDetails);
  const successfullyCreatedJournal = useAppSelector(state => state.journalManagement.successfullyCreatedJournal);
  const journalId = useAppSelector(state => state.journalManagement.journalId);

  const BreadCrumbRoutes = [
    {name: translate('journal_breadcrumb.home'), path: '/journal'},
    {name: translate('journal_breadcrumb.journals'), path: `/journal`},
    {name: translate('journal_breadcrumb.addNewJournal'), path: ''},
  ];

  const stepRoute = (stepNumber: number) => {
    setCurrentStep(stepNumber)
  }

  useEffect(() => {
    if (successfullyCreatedJournal) {
      Storage.local.set('journal_id', journalId);
      navigate(`/journal/${journalId}`);
    }

  }, [successfullyCreatedJournal]);

  const handleSubmitRequest = (requestData) => {

    const requestFinalData = {
      ...stepOneData,
      ...stepTwoData,
      ...requestData
    };

    const formData = new FormData();

    for (const key in requestFinalData) {
      if (Object.prototype.hasOwnProperty.call(requestFinalData, key)) {
        formData.append(key, requestFinalData[key]);
      }
    }
    dispatch(postNewJournal(formData));
  }

  const prevStep = () => {
    if (currentStep > 1) {
      setCurrentStep(currentStep - 1);
    }
  };

  return (
    <>
      <div className="ps-3 pe-3">
        <div className="pt-2 ">
          <Breadcrumb props={BreadCrumbRoutes}/>
        </div>

        <div className="wizard-container justify-content-center">
          <div className="wizard-steps ms-0">
            <div onClick={() => stepRoute(1)}
                 className={`step ${currentStep === 1 ? 'active' : ''} ${JournalDetails?.completedStep >= 1 ? 'completed' : ''}`}>
              <Translate contentKey="journalCreateWorkflow.step1"></Translate>
            </div>
            <div className="horizontal-line"></div>
            <div onClick={() => {
              if (stepActivate.stepOne) {
                stepRoute(2)
              }
            }}
                 className={`step ${currentStep === 2 ? 'active' : ''} ${JournalDetails?.completedStep >= 2 ? 'completed' : ''} ${stepActivate.stepOne ? '' : 'disabled'}`}>
              <Translate contentKey="journalCreateWorkflow.step2"></Translate>
            </div>
            <div className="horizontal-line"></div>
            <div onClick={() => {
              if (stepActivate.stepOne && stepActivate.stepTwo) {
                stepRoute(3)
              }
            }}
                 className={`step ${currentStep === 3 ? 'active' : ''} ${JournalDetails?.completedStep >= 3 ? 'completed' : ''} ${(stepActivate.stepOne && stepActivate.stepTwo) ? '' : 'disabled'}`}>
              <Translate contentKey="journalCreateWorkflow.step3"></Translate>
            </div>
          </div>

        </div>
        {currentStep === 1 &&
          <CreateJournalStep1 stepRoute={stepRoute} stepActivate={stepActivate} setStepActivate={setStepActivate}
                              stepOneData={stepOneData} setStepOneData={setStepOneData}/>}
        {currentStep === 2 &&
          <CreateJournalStep2 prevStep={prevStep} stepActivate={stepActivate} setStepActivate={setStepActivate}
                              stepRoute={stepRoute} setStepTwoData={setStepTwoData} stepTwoData={stepTwoData}/>}
        {currentStep === 3 &&
          <CreateJournalStep3 prevStep={prevStep} stepThreeData={stepThreeData} setStepThreeData={setStepThreeData}
                              handleSubmitRequest={handleSubmitRequest}/>}
      </div>
    </>
  );
};

export default CreateNewJournal;
