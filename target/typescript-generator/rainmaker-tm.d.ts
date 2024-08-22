/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 2.22.595 on 2024-08-22 06:45:44.

export namespace Digit {

    interface CreateUserRequest {
        requestInfo: RequestInfo;
        user: Citizen;
    }

    interface UserResponse {
        responseInfo: ResponseInfo;
        user: Citizen[];
    }

    interface UserSearchRequest {
        RequestInfo: RequestInfo;
        id: number[];
        uuid: string[];
        userName: string;
        name: string;
        mobileNumber: string;
        aadhaarNumber: string;
        pan: string;
        emailId: string;
        fuzzyLogic: boolean;
        active: boolean;
        tenantId: string;
        pageSize: number;
        pageNumber: number;
        sort: string[];
        userType: string;
        roleCodes: string[];
    }

    interface ActionHistory {
        actions: ActionInfo[];
    }

    interface ActionInfo {
        uuid: string;
        createdBy: string;
        isInternal: boolean;
        createdTime: number;
        businessKey: string;
        action: string;
        status: string;
        assignee: string;
        comments: string;
    }

    interface AuditDetails {
        createdBy: string;
        lastModifiedBy: string;
        createdTime: number;
        lastModifiedTime: number;
    }

    interface ServiceDef {
        tenantId: string;
        serviceCode: string;
        serviceName: string;
        description: string;
        metadata: string;
        type: ServiceType;
        keywords: string;
        group: string;
        slaHours: number;
    }

    interface RequestInfo {
        apiId: string;
        ver: string;
        ts: number;
        action: string;
        did: string;
        key: string;
        msgId: string;
        authToken: string;
        correlationId: string;
        userInfo: User;
    }

    interface Citizen {
        id: number;
        uuid: string;
        name: string;
        mobileNumber: string;
        aadhaarNumber: string;
        pan: string;
        emailId: string;
        userName: string;
        password: string;
        active: boolean;
        type: UserType;
        gender: Gender;
        tenantId: string;
        permanentAddress: string;
        roles: PgrRole[];
    }

    interface ResponseInfo {
        apiId: string;
        ver: string;
        ts: number;
        resMsgId: string;
        msgId: string;
        status: string;
    }

    interface User {
        id: number;
        userName: string;
        name: string;
        type: string;
        mobileNumber: string;
        emailId: string;
        roles: Role[];
        tenantId: string;
        uuid: string;
    }

    interface PgrRole {
        name: string;
        code: string;
        tenantId: string;
    }

    interface Role {
        id: number;
        name: string;
        code: string;
    }

    type UserType = "CITIZEN" | "EMPLOYEE" | "SYSTEM" | "BUSINESS";

    type Status = "ACTIVE" | "INACTIVE" | "CANCELLED";

    type ServiceType = "realtime" | "batch" | "blackbox";

    type Gender = "FEMALE" | "MALE" | "OTHERS";

}
