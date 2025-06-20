import * as React from "react";
import { styled } from "@mui/material/styles";
import Button from "@mui/material/Button";
import CloudUploadIcon from "@mui/icons-material/CloudUpload";

const VisuallyHiddenInput = styled("input")({
    clip: "rect(0 0 0 0)",
    clipPath: "inset(50%)",
    height: 1,
    overflow: "hidden",
    position: "absolute",
    bottom: 0,
    left: 0,
    whiteSpace: "nowrap",
    width: 1
});
export default function InputFileUpload({ onChange, error }) {
    return (
        <Button
            component="label"
            role={undefined}
            variant="contained"
            tabIndex={-1}
            startIcon={<CloudUploadIcon />}
            sx={{ bgcolor: error ? "#ff0000" : "#547838" ,
                width: "24.5vw" ,
                borderRadius: "0px",
                height: "7vh",}}
            error={error}
        >
            Dodaj zdjęcie
            <VisuallyHiddenInput type="file" onChange={onChange}/>
        </Button>
    );
}
